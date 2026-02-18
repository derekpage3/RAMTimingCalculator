package com.derekpage.RAMTimingCalculator.ui;

import com.derekpage.RAMTimingCalculator.logging.Logger;

import java.awt.*;
import java.util.Arrays;

//TODO maybe get this crap working.
public class VariableGridLayout extends GridLayout {

    //Keep a cache of the preferred and minimum widths for each column as they are refreshed by calls
    //to preferredLayoutSize() and minimumLayoutSize().
    protected int[] preferredLayoutWidths = new int[0];
    protected int[] minimumLayoutWidths = new int[0];

    public VariableGridLayout() {
        super();
    }

    public VariableGridLayout(int rows, int cols) {
        super(rows, cols);
    }

    public VariableGridLayout(int rows, int cols, int hgap, int vgap) {
        super(rows, cols, hgap, vgap);
    }

    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
Logger.debug("preferredLayoutSize CALLED");
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = super.getRows();
            int ncols = super.getColumns();
            int hgap = super.getHgap();
            int vgap = super.getVgap();

            //If initialized with a static number of rows, fall back to the standard GridLayout logic.
            //TODO maybe we can make this work with rows too in the future?
            if (nrows > 0)
                return super.preferredLayoutSize(parent);

            //Calculate number of rows to render.
            nrows = (ncomponents + ncols - 1) / ncols;

            //Instantiate array to capture widths for each column.
            int[] colWidths = new int[ncols];
            Arrays.fill(colWidths, 0);

            //Run GridLayout's logic for finding width and depth.
            int h = 0;
            for (int i = 0; i < ncomponents; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getPreferredSize();

                int wIndex = (i % ncols);
                if (colWidths[wIndex] < d.width) {
                    colWidths[wIndex] = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }

            //Initialize total width using GridLayout formula for static elements of the width.
            int totalWidth = insets.left + insets.right + ((ncols - 1) * hgap);

            //Add final width of each column and return calculated dimensions.
            for (int i = 0; i < colWidths.length; i++)
                totalWidth += colWidths[i];

            //Optimization - cache the results for later use.
            this.preferredLayoutWidths = colWidths;

            return new Dimension(totalWidth,
                    insets.top + insets.bottom + nrows * h + (nrows - 1) * vgap);
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
Logger.debug("minimumLayoutSize CALLED");
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = super.getRows();
            int ncols = super.getColumns();
            int hgap = super.getHgap();
            int vgap = super.getVgap();

            //If initialized with a static number of rows, fall back to the standard GridLayout logic.
            //TODO maybe we can make this work with rows too in the future?
            if (nrows > 0)
                return super.minimumLayoutSize(parent);

            //Calculate number of rows to render.
            nrows = (ncomponents + ncols - 1) / ncols;

            //Instantiate array to capture widths for each column.
            int[] colWidths = new int[ncols];
            Arrays.fill(colWidths, 0);

            //Run GridLayout's logic for finding width and depth.
            int h = 0;
            for (int i = 0; i < ncomponents; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getMinimumSize();

                int wIndex = (i % ncols);
                if (colWidths[wIndex] < d.width) {
                    colWidths[wIndex] = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }

            //Initialize total width using GridLayout formula for static elements of the width.
            int totalWidth = insets.left + insets.right + ((ncols - 1) * hgap);

            //Add final width of each column and return calculated dimensions.
            for (int i = 0; i < colWidths.length; i++)
                totalWidth += colWidths[i];

            //Optimization - cache the results for later use.
            this.minimumLayoutWidths = colWidths;

            return new Dimension(totalWidth,
                    insets.top + insets.bottom + nrows * h + (nrows - 1) * vgap);
        }
    }


    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
Logger.debug("layoutContainer CALLED");
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = super.getRows();
            int ncols = super.getColumns();
            int hgap = super.getHgap();
            int vgap = super.getVgap();
            boolean ltr = parent.getComponentOrientation().isLeftToRight();

            //Abort if no components.
            if (ncomponents == 0) {
                return;
            }

            //If initialized with a static number of rows, fall back to the standard GridLayout logic.
            //TODO maybe we can make this work with rows too in the future?
            if (nrows > 0) {
                super.layoutContainer(parent);
                return;
            }

            //Calculate row counts.
            nrows = (ncomponents + ncols - 1) / ncols;

            /// Perform the OOB layoutContainer() math first. ///

            // 4370316. To position components in the center we should:
            // 1. get an amount of extra space within Container
            // 2. incorporate half of that value to the left/top position
            // Note that we use truncating division for widthOnComponent
            // The reminder goes to extraWidthAvailable
            int totalGapsWidth = (ncols - 1) * hgap;
            Logger.debug("ncols: " + ncols);
            Logger.debug("hgap: " + hgap);
            Logger.debug("totalGapsWidth: " + totalGapsWidth);
            int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
            Logger.debug("widthWOInsets: " + widthWOInsets);
            int widthOnComponent = (widthWOInsets - totalGapsWidth) / ncols;
            Logger.debug("widthOnComponent: " + widthOnComponent);
            int extraWidthAvailable = (widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) / 2;
            Logger.debug("extraWidthAvailable: " + extraWidthAvailable);

            int totalGapsHeight = (nrows - 1) * vgap;
            int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
            int heightOnComponent = (heightWOInsets - totalGapsHeight) / nrows;
            int extraHeightAvailable = (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) / 2;

            //Keeps track of the final column widths to use.
            int[] finalColWidths = null;

            //1. Calculate total preferred width.
            int totalWidth = 0;
            for (int i = 0; i < this.preferredLayoutWidths.length; i++) {
                totalWidth += this.preferredLayoutWidths[i];
            }
            Logger.debug("preferredLayoutWidths.length: " + this.preferredLayoutWidths.length + ", preferredWidth: " + totalWidth);

            //If the total preferred width <= to the available width then use it.
            if (totalWidth <= ((widthOnComponent * ncols) + extraWidthAvailable))
                finalColWidths = this.preferredLayoutWidths;

            //2. If the calculated total width exceeds the uniform component width calculated by the OOB logic, then
            //we can't fit our components within their preferred widths.  Attempt to use minimum widths.
            if (finalColWidths == null) {
                totalWidth = 0;
                for (int i = 0; i < this.minimumLayoutWidths.length; i++) {
                    totalWidth += this.minimumLayoutWidths[i];
                }
                Logger.debug("minimumLayoutWidths: " + this.minimumLayoutWidths.length + ", minimumWidth: " + totalWidth);

                //If the total minimum width <= to the available width then use it.
                if (totalWidth <= ((widthOnComponent * ncols) + extraWidthAvailable))
                    finalColWidths = this.minimumLayoutWidths;
            }

            //If even the minimum width is too large, then we'll just have to fall back to the OOB logic's width.
            if (finalColWidths == null) {
                super.layoutContainer(parent);
                return;
            }

            /// Padding - determine if we need to add any width to the columns to fill the container ///

            //Total width available for components is the parent container's width, minus insets, minus total with
            //of the gaps, plus any extra width space previously calculated to be available.
            int totalCompSpaceWidth = (widthWOInsets - totalGapsWidth) + extraWidthAvailable;

            /// Padding - if our total widths are less than the total width of the container space then we need to add the excess to columns.
            Logger.debug("totalWidth: " + totalWidth + ", totalCompSpaceWidth: " + totalCompSpaceWidth);
            Logger.debug("Parent width: " + parent.getWidth());
            if (totalWidth < totalCompSpaceWidth) {

                Logger.debug("BEFORE Col widths:");
                for (int i = 0; i < finalColWidths.length; i++)
                    Logger.debug(finalColWidths[i]);

                //We're simply going to "round robin" adding an extra pixel width to each component until
                //the deficit is depleted.
                int excessWidth = totalCompSpaceWidth - totalWidth;
                int indx = 0;
                do {
                    finalColWidths[indx] += 1;
                    excessWidth--;
                    indx = (indx+1) % ncols;
                } while (excessWidth > 0);

                Logger.debug("AFTER Col widths:");
                for (int i = 0; i < finalColWidths.length; i++)
                    Logger.debug(finalColWidths[i]);

                Logger.debug("AFTER totalWidth: " + totalWidth + ", totalCompSpaceWidth: " + totalCompSpaceWidth);
            }

            //If we get here then we were able to use our custom widths to lay out the columns.  Use the OOB logic
            //to lay out each cell with the varying column widths.
            if (ltr) {
                for (int c = 0, x = insets.left + extraWidthAvailable; c < ncols; c++) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows; r++, y += heightOnComponent + vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, finalColWidths[c], heightOnComponent);
                        }
                    }
                    if (c+1 < ncols)
                        x += (finalColWidths[c] + hgap);
                }
            } else {
                for (int c = 0, x = (parent.getWidth() - insets.right - finalColWidths[finalColWidths.length-1]) - extraWidthAvailable; c < ncols; c++) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows; r++, y += heightOnComponent + vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, finalColWidths[finalColWidths.length-1-c], heightOnComponent);
                        }
                    }
                    if (c+1 < ncols)
                        x -= (finalColWidths[finalColWidths.length-c-2] + hgap);
                }
            }
        }
    }
}
