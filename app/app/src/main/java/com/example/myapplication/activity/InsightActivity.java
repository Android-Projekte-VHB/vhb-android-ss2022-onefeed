package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.insight.ReadingDay;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// Insight = Konsumverhalten
public class InsightActivity extends AppCompatActivity {

    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        // Title-bar
        setSupportActionBar(findViewById(R.id.app_bar_toolbar));

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.app_bar_toolbar);
        toolbar.setNavigationOnClickListener(l -> finish());

        // Chart
        initializeChart();
        initializeChartControls();

        // Settings
        initializeSettings();
    }

    private void initializeSettings() {
        MaterialSwitch limitArticlesSwitch = findViewById(R.id.insight_limit_articles_switch);
        Slider limitArticlesSlider = findViewById(R.id.insight_limit_articles_slider);
        TextView limitArticlesDescription = findViewById(R.id.insight_limit_articles_description);

        // Todo: load preferences from settings
        limitArticlesSwitch.setChecked(true);

        limitArticlesSwitch.setOnCheckedChangeListener((button, newValue) -> {
            limitArticlesSlider.setEnabled(newValue);
            limitArticlesDescription.setEnabled(newValue);
        });
    }

    private void initializeChartControls() {
        // Views
        MaterialButton chartWeekButton = findViewById(R.id.insight_chart_toggle_week);
        MaterialButton chartMonthButton = findViewById(R.id.insight_chart_toggle_month);
        MaterialButton chartYearButton = findViewById(R.id.insight_chart_toggle_year);

        // Listeners
        LocalDateTime now = LocalDateTime.now();
        int currentDayOfYear = now.getDayOfYear();

        // Week button
        chartWeekButton.setChecked(true);
        chartWeekButton.setOnClickListener(l -> activateWeekView(now, currentDayOfYear));

        // Month button
        chartMonthButton.setOnClickListener(l -> activateMonthView(now, currentDayOfYear));

        // Year button
        chartYearButton.setOnClickListener(l -> activateYearView(now));
    }

    private void activateYearView(LocalDateTime now) {
        float barWidth = ResourcesCompat.getFloat(
                this.getResources(), R.dimen.insight_chart_bar_width_year
        );

        int amountDaysInYear = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_YEAR);

        // Adjust the axis min & max
        // no padding (value too large / small for chart because out of current year)
        this.chart.getXAxis().setAxisMinimum(1); // barwidth for padding
        this.chart.getXAxis().setAxisMaximum(amountDaysInYear);

        // Format the labels according to months
        this.chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Chart x-axis granularity has to be set to 1 for this to work:
                // MPAndroidChart iterates through every x-value so float values should not
                // be a possibility!
                LocalDateTime providedDate = now.withDayOfYear((int)value);
                return providedDate.getMonth().getDisplayName(
                        TextStyle.SHORT_STANDALONE, Locale.getDefault()
                );
            }
        });

        if (this.chart.getXAxis().getLabelCount() != 12) {
            this.chart.getXAxis().setLabelCount(12);
            this.chart.getBarData().setBarWidth(barWidth);
        }
        refreshChart();
    }

    private void activateMonthView(LocalDateTime now, int currentDayOfYear) {
        float barWidth = ResourcesCompat.getFloat(this.getResources(), R.dimen.insight_chart_bar_width_month);

        int currentDayOfMonth = now.getDayOfMonth();
        int startOfCurrentMonth = currentDayOfYear - currentDayOfMonth;
        int amountDaysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DATE);

        // Adjust the axis min & max
        float chartPaddingLeft = barWidth;
        float chartPaddingRight = barWidth;
        if (now.getMonth() == Month.JANUARY) chartPaddingLeft = 0;
        if (now.getMonth() == Month.DECEMBER) chartPaddingRight = 0;

        this.chart.getXAxis().setAxisMinimum(startOfCurrentMonth - chartPaddingLeft);
        this.chart.getXAxis().setAxisMaximum(startOfCurrentMonth + amountDaysInMonth + chartPaddingRight);

        // Format the labels according to days in a month
        this.chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Chart x-axis granularity has to be set to 1 for this to work:
                // MPAndroidChart iterates through every x-value so float values should not
                // be a possibility!
                return String.valueOf(now.withDayOfYear((int)value).getDayOfMonth());
            }
        });

        // Displaying 30 dates would not fit the chart space
        if (this.chart.getXAxis().getLabelCount() != 15) {
            this.chart.getXAxis().setLabelCount(15);
            this.chart.getBarData().setBarWidth(barWidth);
        }
        refreshChart();
    }

    private void activateWeekView(LocalDateTime now, int currentDayOfYear) {
        float barWidth = ResourcesCompat.getFloat(this.getResources(), R.dimen.insight_chart_bar_width_week);

        int currentDayOfWeek = now.getDayOfWeek().getValue();
        int startOfCurrentWeek = currentDayOfYear - currentDayOfWeek + 1;

        // Adjust the axis min & max
        float chartPaddingLeft = barWidth;
        float chartPaddingRight = barWidth;
        if (now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 7) chartPaddingLeft = 0;
        if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 25) chartPaddingRight = 0;

        this.chart.getXAxis().setAxisMinimum(startOfCurrentWeek - chartPaddingLeft);
        this.chart.getXAxis().setAxisMaximum(startOfCurrentWeek + 6 + chartPaddingRight); // 6 more days

        // Format the labels according to weekdays
        this.chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Chart x-axis granularity has to be set to 1 for this to work:
                // MPAndroidChart iterates through every x-value so float values should not
                // be a possibility!
                try {
                    DayOfWeek dayOfWeek = DayOfWeek.of((int) value - startOfCurrentWeek + 1);
                    return dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault());
                } catch (RuntimeException exception) {
                    // Possibly an MPANdroid bug: value provided for the formatter is > than the
                    // previously set minimum and maximum of the charts
                    // Workaround: Catch exception for these cases...
                    return "";
                }
            }
        });

        if (this.chart.getXAxis().getLabelCount() != 7) {
            this.chart.getBarData().setBarWidth(barWidth);
            this.chart.getXAxis().setLabelCount(7);
        }

        refreshChart();
    }

    private void refreshChart() {
        this.chart.fitScreen();
        this.chart.invalidate();
    }

    private void initializeChart() {
        this.chart = findViewById(R.id.insight_chart);

        // Data
        List<BarEntry> sampleEntries = generateSampleEntries();
        BarDataSet dataSet = new BarDataSet(sampleEntries, "");
        // Todo: set text color for night mode
        dataSet.setValueTextColor(MaterialColors.getColor(chart, com.google.android.material.R.attr.colorOnSurface));
        dataSet.setColor(MaterialColors.getColor(chart, androidx.transition.R.attr.colorPrimary));
        // - axis dependency is required because the axis is being adjusted
        dataSet.setAxisDependency(chart.getAxisRight().getAxisDependency());

        // Bar settings
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.3f);

        // Axis settings
        // - Disable unused axis
        this.chart.getAxisLeft().setEnabled(false);

        // - Dashed lines
        this.chart.getAxisRight().enableGridDashedLine(4, 4, 0);
        this.chart.getXAxis().enableGridDashedLine(4, 4, 0);

        // - x axis
        this.chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        this.chart.getXAxis().setGranularity(1);

        // y axis
        this.chart.getAxisRight().setAxisMinimum(0);

        // Chart settings
        this.chart.setData(barData);
        this.chart.getLegend().setEnabled(false);
        this.chart.getDescription().setEnabled(false);

        // Viewport settings
        // - viewport has to be adjusted after the data has been loaded (setData) to work
        this.chart.fitScreen();
        this.chart.setVisibleXRange(0, 7);
        this.chart.setTouchEnabled(false);
        this.chart.setFitBars(true);

        activateWeekView(LocalDateTime.now(), LocalDateTime.now().getDayOfYear());
    }

    @NonNull
    private List<BarEntry> generateSampleEntries() {
        LocalDateTime now = LocalDateTime.now();
        ReadingDay exampleDayToday = new ReadingDay(now.getDayOfYear(), 12);
        ReadingDay exampleDayYesterday = new ReadingDay(now.getDayOfYear()-1, 0);
        ReadingDay exampleDayMinus3 = new ReadingDay(now.getDayOfYear()-2, 24);
        ReadingDay exampleDayMinus4 = new ReadingDay(now.getDayOfYear()-3, 11);
        ReadingDay exampleDayPlus2 = new ReadingDay(now.getDayOfYear()+2, 8);
        ReadingDay exampleDayEndOfMonth = new ReadingDay(now.getDayOfYear()+22, 8);

        List<ReadingDay> dataObjects = Arrays.asList(
                exampleDayToday, exampleDayYesterday, exampleDayMinus3, exampleDayMinus4, exampleDayPlus2, exampleDayEndOfMonth
        );

        List<BarEntry> entries = new ArrayList<>();
        for (ReadingDay data : dataObjects) {
            entries.add(new BarEntry(data.getDayOfYear(), data.getAmountArticlesRead()));
        }
        
        return entries;
    }
}