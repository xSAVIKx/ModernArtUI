package org.moma.modernartui;

import java.util.Random;

public class ColumnSizeGenerator {
    private final int columnsAmount;
    private final int rowWidth;
    private final int rowHeight;
    private final Random random;
    private final boolean sameElementWidth;
    private final boolean sameElementHeight;
    private final int minimumElementWidth;
    private final int minimumElementHeight;
    private int widthLeft;
    private int heightLeft;
    private int elementsLeft;

    public ColumnSizeGenerator(int columnsAmount, int rowWidth, int rowHeight, boolean sameElementWidth, boolean sameElementHeight, int minimumElementWidth, int minimumElementHeight) {
        this.columnsAmount = columnsAmount;
        this.rowWidth = rowWidth;
        this.rowHeight = rowHeight;
        this.sameElementWidth = sameElementWidth;
        this.sameElementHeight = sameElementHeight;
        this.minimumElementHeight = minimumElementHeight;
        this.minimumElementWidth = minimumElementWidth;
        random = new Random();
        clearElementsCount();
        widthLeft = rowWidth;
        heightLeft = rowHeight;
        checkDimensions();
    }

    private void checkDimensions() {
        if (!sameElementWidth && rowWidth < columnsAmount * minimumElementWidth) {
            throw new IllegalArgumentException("RowWidth doesn't match. RowWidth=" + rowWidth + ".Row should hold at least " + columnsAmount + " elements with minimum width=" + minimumElementWidth + ".");
        }
        if (!sameElementHeight && rowHeight < columnsAmount * minimumElementHeight) {
            throw new IllegalArgumentException("RowHeight doesn't match. RowHeight=" + rowHeight + ".Row should hold at least " + columnsAmount + " elements with minimum width=" + minimumElementHeight + ".");
        }
    }

    public boolean hasNext() {
        return elementsLeft != 0;
    }

    public void clearElementsCount() {
        elementsLeft = columnsAmount;
        widthLeft = rowWidth;
        heightLeft = rowHeight;
    }

    public Size nextSize() {
        int nextWidth = nextWidth();
        int nextHeight = nextHeight();
        elementsLeft--;
        return new Size(nextWidth, nextHeight);
    }

    private int nextWidth() {
        int nextWidth = 0;
        if (isLastElement()) {
            nextWidth = widthLeft;
        } else if (sameElementWidth) {
            nextWidth = rowWidth / columnsAmount;
        } else {
            nextWidth = randomInteger(minimumElementWidth, widthLeft - (elementsLeft - 1) * minimumElementWidth);
        }
        widthLeft -= nextWidth;
        return nextWidth;
    }


    private int nextHeight() {
        int nextHeight = 0;
        if (isLastElement()) {
            nextHeight = heightLeft;
        } else if (sameElementHeight) {
            return rowHeight;
        } else {
            nextHeight = randomInteger(minimumElementHeight, heightLeft - (elementsLeft - 1) * minimumElementHeight);
        }
        heightLeft -= nextHeight;
        return nextHeight;
    }

    private int randomInteger(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private boolean isLastElement() {
        return elementsLeft == 1;
    }

    public static final class Size {
        private final int width;
        private final int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
