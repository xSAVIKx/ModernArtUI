package org.moma.modernartui;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int PALETTE_ITEMS_AMOUNT = 12;
    private final int ELEMENTS_IN_ROW_AMOUNT = 3;
    private final int ROWS_AMOUNT = PALETTE_ITEMS_AMOUNT / ELEMENTS_IN_ROW_AMOUNT;
    private final int MAX_COLOR_VALUE = 255;
    private static final String MORE_INFORMATION_FRAGMENT_TAG = "moreInformation";

    private Random random = new Random();
    private ColumnSizeGenerator sizeGenerator;
    private Point paletteSize;

    private LinearLayout paletteLayout;
    private SeekBar seekBar;
    private List<View> rectangles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paletteLayout = (LinearLayout) findViewById(R.id.palette);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int startPoint;
            private int endPoint;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPoint = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                endPoint = seekBar.getProgress();
                int progress = endPoint - startPoint;
                for (View view : rectangles) {
                    int viewBackgroundColor = ((ColorDrawable) view.getBackground()).getColor();
                    if (viewBackgroundColor != Color.WHITE) {
                        int newRed = (Color.red(viewBackgroundColor) + progress) % MAX_COLOR_VALUE;
                        int newGreen = (Color.green(viewBackgroundColor) + progress) % MAX_COLOR_VALUE;
                        int newBlue = (Color.blue(viewBackgroundColor) + progress) % MAX_COLOR_VALUE;
                        int newColor = Color.rgb(newRed, newGreen, newBlue);
                        view.setBackgroundColor(newColor);
                    }
                }
            }
        });
        initFields();
        initPaletteItems();
    }

    private void initFields() {
        paletteSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(paletteSize);
        paletteSize.set(paletteSize.x - seekBar.getWidth(), paletteSize.y - seekBar.getHeight());
        int rowHeight = paletteSize.y / ROWS_AMOUNT;
        int minimumColumnWidth = paletteSize.x >> ELEMENTS_IN_ROW_AMOUNT;
        sizeGenerator = new ColumnSizeGenerator(ELEMENTS_IN_ROW_AMOUNT, paletteSize.x, rowHeight, false, true, minimumColumnWidth, 0);
    }


    private void initPaletteItems() {
        rectangles = new ArrayList<>(PALETTE_ITEMS_AMOUNT);
        int[] backgroundColors = getRandomColors(PALETTE_ITEMS_AMOUNT);
        for (int i = 0; i < ROWS_AMOUNT; i++) {
            LinearLayout row = (LinearLayout) getLayoutInflater().inflate(R.layout.palette_row, paletteLayout, false);
            for (int j = 0; j < ELEMENTS_IN_ROW_AMOUNT; j++) {
                View view = getLayoutInflater().inflate(R.layout.rectangle_item, row, false);
                view.setBackgroundColor(backgroundColors[i * ELEMENTS_IN_ROW_AMOUNT + j]);
                rectangles.add(view);
                row.addView(view, getColumnLayoutParameters(sizeGenerator.nextSize()));
            }
            paletteLayout.addView(row);
            sizeGenerator.clearElementsCount();
        }
    }

    private GridLayout.LayoutParams getColumnLayoutParameters(ColumnSizeGenerator.Size size) {
        GridLayout.LayoutParams p = new GridLayout.LayoutParams();
        p.width = size.getWidth();
        p.height = size.getHeight();
        p.setMargins(5, 5, 5, 5);
        return p;
    }

    private int[] getRandomColors(int amount) {
        int[] colors = new int[amount];
        int randomWhiteColorLocation = random.nextInt(amount);
        for (int i = 0; i < amount; i++) {
            if (i == randomWhiteColorLocation) {
                colors[i] = Color.WHITE;
            } else {
                int color = Color.rgb(random.nextInt(MAX_COLOR_VALUE + 1), random.nextInt(MAX_COLOR_VALUE + 1), random.nextInt(MAX_COLOR_VALUE + 1));
                while (color == Color.WHITE) {
                    color = Color.rgb(random.nextInt(MAX_COLOR_VALUE + 1), random.nextInt(MAX_COLOR_VALUE + 1), random.nextInt(MAX_COLOR_VALUE + 1));
                }
                colors[i] = color;
            }
        }
        return colors;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            MoreInformationFragment informationFragment = new MoreInformationFragment();
            informationFragment.show(getFragmentManager(), MORE_INFORMATION_FRAGMENT_TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
