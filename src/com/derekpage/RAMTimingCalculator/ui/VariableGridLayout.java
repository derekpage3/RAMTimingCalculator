package com.derekpage.RAMTimingCalculator.ui;

import com.derekpage.RAMTimingCalculator.logging.Logger;

import java.awt.*;
import java.util.Arrays;
//TODO maybe get this crap working.
public class VariableGridLayout extends GridLayout {

    //Keep a cache of the preferred and minimum widths for each column as they are refreshed by calls
    //to preferredLayoutSize() and minimumLayoutSize().
    protected int[] preferredLayoutWidths = null;
    protected int[] minimumLayoutWidths = null;

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
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = super.getRows();
            int ncols = super.getColumns();
            int hgap = super.getHgap();
            int vgap = super.getVgap();

            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }

            //Instantiate array to capture widths for each column.
            int[] colWidths = new int[ncols];
            Arrays.fill(colWidths, 0);

            //Run GridLayout's logic for finding width and depth.
            int h = 0;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getPreferredSize();

                int wIndex = (i %  ncols);
                if (colWidths[wIndex] < d.width) {
                    colWidths[wIndex] = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }

            //Calculate final width (minus the hgap) and return calculated dimensions.
            int colWidth = 0;
            for (int i = 0; i < colWidths.length; i++)
                colWidth += colWidths[i];

            //Update the cached preferred column widths.
            this.preferredLayoutWidths = colWidths;

            return new Dimension(insets.left + insets.right + colWidth + (ncols-1)*hgap,
                    insets.top + insets.bottom + nrows*h + (nrows-1)*vgap);
        }
    }

    /*public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
Logger.debug("CALLED preferredLayoutSize");
            Insets insets = parent.getInsets();
Logger.debug("insets.left: " + insets.left);
Logger.debug("insets.right: " + insets.right);
Logger.debug("insets.top: " + insets.top);
Logger.debug("insets.bottom: " + insets.bottom);
            int ncomponents = parent.getComponentCount();
Logger.debug("ncomponents: " + ncomponents);
            int rows = this.getRows();
            int cols = this.getColumns();
            int nrows = rows;
Logger.debug("nrows: " + nrows);
            int ncols = cols;
Logger.debug("ncols: " + ncols);
            int hgap = this.getHgap();
Logger.debug("hgap: " + hgap);
            int vgap = this.getVgap();
Logger.debug("vgap: " + vgap);

            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
Logger.debug("nrows calcd: " + nrows);
            }
            int w = 0;
            int h = 0;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getPreferredSize();
                if (i != 0 && ((i % cols) == 0))
                    Logger.debug("");
Logger.debug("comp " + i + " (" + comp.getName() + "): " + d.getWidth());
                if (w < d.width) {
                    w = d.width;
Logger.debug("\tw increasing: " + w + ", via " + comp.getName());
                }
                if (h < d.height) {
                    h = d.height;
Logger.debug("\th increasing: " + h);
                }
            }
Logger.debug("RETURNING preferredLayoutSize");
            return new Dimension(insets.left + insets.right + ncols*w + (ncols-1)*hgap,
                    insets.top + insets.bottom + nrows*h + (nrows-1)*vgap);
        }
    }*/

    /*
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
Logger.debug("ncomponents: " + ncomponents);
            int rows = this.getRows();
            int nrows = rows;
Logger.debug("nrows: " + nrows);
            int cols = this.getColumns();
            int ncols = cols;
Logger.debug("ncols: " + ncols);
            int hgap = this.getHgap();
Logger.debug("hgap: " + hgap);
            int vgap = this.getVgap();
Logger.debug("vgap: " + vgap);

            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
Logger.debug("nrows calcd: " + nrows);
            }
            int w = 0;
            int h = 0;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getMinimumSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
Logger.debug("RETURNING minimumLayoutSize");
            return new Dimension(insets.left + insets.right + ncols*w + (ncols-1)*hgap,
                    insets.top + insets.bottom + nrows*h + (nrows-1)*vgap);
        }
    }*/

    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = super.getRows();
            int ncols = super.getColumns();
            int hgap = super.getHgap();
            int vgap = super.getVgap();

            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }

            //Instantiate array to capture widths for each column.
            int[] colWidths = new int[ncols];
            Arrays.fill(colWidths, 0);

            //Run GridLayout's logic for minimumLayoutSize.
            int h = 0;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getMinimumSize();

                int wIndex = (i %  ncols);
                if (colWidths[wIndex] < d.width) {
                    colWidths[wIndex] = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }

            //Calculate final width (minus the hgap) and return calculated dimensions.
            int colWidth = 0;
            for (int i = 0; i < colWidths.length; i++)
                colWidth += colWidths[i];

            //Update the cached minimum column widths.
            this.minimumLayoutWidths = colWidths;

            return new Dimension(insets.left + insets.right + colWidth + (ncols-1)*hgap,
                    insets.top + insets.bottom + nrows*h + (nrows-1)*vgap);
        }
    }

    /*public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
Logger.debug("CALLED layoutContainer");
            Insets insets = parent.getInsets();
Logger.debug("insets.left: " + insets.left);
Logger.debug("insets.right: " + insets.right);
Logger.debug("insets.top: " + insets.top);
Logger.debug("insets.bottom: " + insets.bottom);
            int ncomponents = parent.getComponentCount();
Logger.debug("ncomponents: " + ncomponents);
            int nrows = this.getRows();
Logger.debug("nrows: " + nrows);
            int ncols = this.getColumns();
Logger.debug("ncols: " + ncols);
            boolean ltr = parent.getComponentOrientation().isLeftToRight();

            if (ncomponents == 0) {
Logger.debug("RETURNING layoutContainer EARLY");
                return;
            }
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
Logger.debug("nrows calcd: " + nrows);
            }
            // 4370316. To position components in the center we should:
            // 1. get an amount of extra space within Container
            // 2. incorporate half of that value to the left/top position
            // Note that we use trancating division for widthOnComponent
            // The reminder goes to extraWidthAvailable
            int totalGapsWidth = (ncols - 1) * this.getHgap();
Logger.debug("totalGapsWidth: " + totalGapsWidth);
            int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
Logger.debug("widthWOInsets: " + widthWOInsets);
            int widthOnComponent = (widthWOInsets - totalGapsWidth) / ncols;
Logger.debug("widthOnComponent: " + widthOnComponent);
            int extraWidthAvailable = (widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) / 2;
Logger.debug("extraWidthAvailable: " + extraWidthAvailable);

            int totalGapsHeight = (nrows - 1) * this.getVgap();
            int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
            int heightOnComponent = (heightWOInsets - totalGapsHeight) / nrows;
            int extraHeightAvailable = (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) / 2;
            if (ltr) {
                for (int c = 0, x = insets.left + extraWidthAvailable; c < ncols ; c++, x += widthOnComponent + this.getHgap()) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + this.getVgap()) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
                        }
                    }
                }
            } else {
                for (int c = 0, x = (parent.getWidth() - insets.right - widthOnComponent) - extraWidthAvailable; c < ncols ; c++, x -= widthOnComponent + this.getHgap()) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + this.getVgap()) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
                        }
                    }
                }
            }
        }
        Logger.debug("RETURNING layoutContainer");
    }*/

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
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

            //Calculate row or column counts.
            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }

            /// Perform the OOB layoutContainer() math first. ///

            // 4370316. To position components in the center we should:
            // 1. get an amount of extra space within Container
            // 2. incorporate half of that value to the left/top position
            // Note that we use truncating division for widthOnComponent
            // The reminder goes to extraWidthAvailable
            int totalGapsWidth = (ncols - 1) * hgap;
            int widthWOInsets = parent.getWidth() - (insets.left + insets.right);
            int widthOnComponent = (widthWOInsets - totalGapsWidth) / ncols;
            int extraWidthAvailable = (widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) / 2;

            int totalGapsHeight = (nrows - 1) * vgap;
            int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
            int heightOnComponent = (heightWOInsets - totalGapsHeight) / nrows;
            int extraHeightAvailable = (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) / 2;

            //Keeps track of the final column widths to use.
            int[] finalColWidths = null;
Logger.debug("widthOnComponent: " + widthOnComponent);
            //Calculate total width based on column preferred widths.
            int totalWidth = 0;
            for (int i = 0; i < this.preferredLayoutWidths.length; i++) {
                totalWidth += this.preferredLayoutWidths[i];
            }
Logger.debug("preferredLayoutWidths: " + this.preferredLayoutWidths.length +  ", preferredWidth: " + totalWidth);
            finalColWidths = this.preferredLayoutWidths;

            //If the calculated total width exceeds the uniform component width calculated by the OOB logic, then
            //we can't fit our components within their preferred widths.  Attempt to use minimum widths.
            if (totalWidth > (widthOnComponent * ncols)) {
                for (int i = 0; i < this.minimumLayoutWidths.length; i++) {
                    totalWidth += this.minimumLayoutWidths[i];
                }
Logger.debug("minimumLayoutWidths: " + this.minimumLayoutWidths.length +  ", minimumWidth: " + totalWidth);
                finalColWidths = this.minimumLayoutWidths;

                //If even the minimum width is too large, then we'll just have to fall back to the OOB logic's width.
                //We accomplish this by setting the final column widths array to use to have the same value for all
                //columns, which will effectively generate the OOB result.
                if (totalWidth > (widthOnComponent * ncols)) {
Logger.debug("Reverting to OOB width: " + widthOnComponent);
                    finalColWidths = new int[ncols];
                    Arrays.fill(finalColWidths, widthOnComponent);
                }
            }

            if (ltr) {
Logger.debug("ncols: " + ncols);
Logger.debug("finalColWidths: " + finalColWidths.length);
                for (int c = 0, x = insets.left + extraWidthAvailable; c < ncols ; c++, x += finalColWidths[0] + hgap) {
Logger.debug("c: " + c + ", finalColWidths[c]: " + finalColWidths[c]);
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, finalColWidths[c], heightOnComponent);
                        }
                    }
Logger.debug("c+1: " + (c+1) + ", finalColWidths[c+1]: " + finalColWidths[c+1]);
                }
            } else {
                for (int c = 0, x = (parent.getWidth() - insets.right - finalColWidths[0]) - extraWidthAvailable; c < ncols ; c++, x -= finalColWidths[c] + hgap) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, finalColWidths[c], heightOnComponent);
                        }
                    }
                }
            }
        }
    }
}
