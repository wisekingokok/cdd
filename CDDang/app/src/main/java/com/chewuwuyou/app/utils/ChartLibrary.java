
package com.chewuwuyou.app.utils;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ChartLibrary {
    public static enum CHARTTYPE {
        LINE, PILLOW
    }

    /**
     * Get the graphic View.
     * 
     * @param context 上下文
     * @param type 绘制的图类型
     * @param xValues X轴数值
     * @param yValues Y轴数值
     * @param names X轴显示文字
     */
    public static GraphicalView execute(
            Context context, CHARTTYPE type,
            double[] xValues, double[] yValues, String[] names) {
        // int[] colors = new int[] { Color.RED, Color.YELLOW, Color.BLUE };
        // DefaultRenderer renderer = buildCategoryRenderer(colors);
        // CategorySeries categorySeries = new CategorySeries("Vehicles Chart");
        // categorySeries.add("cars ", 30);
        // categorySeries.add("trucks", 20);
        // categorySeries.add("bikes ", 60);
        // return ChartFactory//.getLineChartIntent(context, dataset, renderer);
        // .getPieChartIntent(context, categorySeries, renderer, "图形展示");

        // Intent intent = null;
        GraphicalView gView = null;
        if (type == CHARTTYPE.LINE) {
            String[] titles = new String[] {
                    "月份消费趋势"
            };
            List<double[]> x = new ArrayList<double[]>();
            for (int i = 0, len = titles.length; i < len; i++) {
                x.add(xValues);
            }
            List<double[]> values = new ArrayList<double[]>();
            for (int i = 0, len = titles.length; i < len; i++) {
                values.add(yValues);
            }

            double[] calc = calculateMinAndMax(xValues, yValues);

            int[] colors = new int[] {
                    Color.parseColor("#04d504")
            };
            PointStyle[] styles = new PointStyle[] {
                    PointStyle.CIRCLE
            };
            XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
            // 安放name
            for (int i = 0, len = names.length; i < len; i++) {
                renderer.addXTextLabel(xValues[i], names[i]);
            }

            int length = renderer.getSeriesRendererCount();
            for (int i = 0; i < length; i++) {
                ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
            }
            setChartSettings(renderer, "月份消费趋势图", "月份", "", /*
                                                             * 0.5, 12.5, 0, 40,
                                                             */
                    calc[0], calc[1], calc[2], calc[3],
                    Color.parseColor("#434343")
                    , Color.parseColor("#434343"));
            renderer.setAxesColor(Color.parseColor("#434343"));
            renderer.setXLabels(0);
            renderer.setYLabels(10);
            renderer.setShowGrid(true);
            renderer.setGridColor(Color.parseColor("#818181"));

            renderer.setXLabelsAlign(Align.CENTER);
            renderer.setYLabelsAlign(Align.RIGHT);
            renderer.setZoomButtonsVisible(false);
            renderer.setMargins(new int[] {
                    40, 60, 55, 10
            });
            renderer.setPanEnabled(true, false);// 表盘移动
            renderer.setZoomEnabled(false, false);
            renderer.setClickEnabled(true);
            // 设置移动
            renderer.setPanEnabled(true);
            // 坐标滑动上、下限
            renderer.setPanLimits(new double[] {
                    calc[0], calc[1], calc[2], calc[3]
            });
            // 设置放大
            renderer.setZoomEnabled(false);
            // 设置是否显示背景颜色
            renderer.setApplyBackgroundColor(false);
            // 去掉折线图的背景颜色
            renderer.setMarginsColor(0x00ffffff);
            XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
            gView = ChartFactory.getLineChartView(context, dataset, renderer);
        } else if (type == CHARTTYPE.PILLOW) {

            double[] calc = calculateMinAndMax(xValues, yValues);

            XYMultipleSeriesRenderer renderer = getBarDemoRenderer();
            renderer.setXLabels(12);
            renderer.setPanEnabled(true);
            renderer.setPanLimits(new double[] {
                    calc[0], calc[1], calc[2], calc[3]
            });

            renderer.setPanEnabled(true, false);// 表盘移动
            renderer.setZoomEnabled(false, false);
            renderer.setClickEnabled(true);
            renderer.setShowGridX(true);
            renderer.setGridColor(Color.parseColor("#aaafaf"));
            // 设置是否显示背景颜色
            renderer.setApplyBackgroundColor(true);
            // 去掉折线图的背景颜色
            renderer.setMarginsColor(0x00ffffff);
            // setChartSettings(renderer,0.5,10.5,0,210);
            setChartSettings(renderer, calc[0], calc[1], calc[2], calc[3]);

            // 安放name
            for (int i = 0, len = names.length; i < len; i++) {
                renderer.addXTextLabel(xValues[i], names[i]);
            }

            // intent = ChartFactory.getBarChartIntent(context,
            // getBarDemoDataset(), renderer, Type.DEFAULT);
            gView = ChartFactory.getBarChartView(
                    context, getBarDemoDataset(xValues, yValues), renderer, Type.DEFAULT);
        }

        gView.setBackgroundColor(Color.parseColor("#efeff4"));
        // return intent;
        return gView;

    }

    private static double[] calculateMinAndMax(double xValues[], double yValues[]) {
        double xMin = 0, xMax = 0, yMin = 0, yMax = 0;
        if (null != xValues)
            for (int i = 0, len = xValues.length; i < len; i++) {
                xMin = Math.min(xMin, xValues[i]);
                xMax = Math.max(xMax, xValues[i]);
            }
        if (null != yValues)
            for (int i = 0, len = yValues.length; i < len; i++) {
                yMin = Math.min(yMin, yValues[i]);
                yMax = Math.max(yMax, yValues[i]);
            }

        double fxMin = 0, fxMax = 0, fyMin = 0, fyMax = 0;
        if (xMin == 0 && xMax == 0) {
            fxMin = 0;
            fxMax = 1;
        } else if (xMin == xMax) {
            fxMin = 0;
            fxMax = xMax * 2;
        } else if (xMin - (xMax - xMin) / 25 < 0) {
            fxMin = 0;
            fxMax = xMax + (xMax - xMin) / 25;
        } else {
            fxMin = xMin - (xMax - xMin) / 25;
            fxMax = xMax + (xMax - xMin) / 25;

        }
        if (yMin == 0 && yMax == 0) {
            fyMin = 0;
            fyMax = 1;
        } else if (yMin == yMax) {
            fyMin = 0;
            fyMax = xMax * 2;
        } else if (yMin - (yMax - yMin) / 25 < 0) {
            fyMin = 0;
            fyMax = yMax + (yMax - yMin) / 25;
        } else {
            fxMin = yMin - (yMax - yMin) / 25;
            fxMax = yMax + (yMax - yMin) / 25;
        }
        return new double[] {
                fxMin, fxMax, fyMin, fyMax
        };
    }

    /*************** ##pragma -- Bar Chart Method Area ****************************/

    private static XYMultipleSeriesDataset getBarDemoDataset(
            double[] xValues, double[] yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        //Random r = new Random();
        XYSeries series = new XYSeries("消费统计");
        for (int k = 0; k < yValues.length; k++) {
            series.add(xValues[k], yValues[k]);
        }
        dataset.addSeries(series);
        return dataset;
    }

    public static XYMultipleSeriesRenderer getBarDemoRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(22);
        renderer.setChartTitleTextSize(22);
        renderer.setYLabels(5);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(22);
        renderer.setMargins(new int[] {
                40, 60, 55, 10
        });
        renderer.setBarSpacing(0.5);
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.parseColor("#eeba06"));
        renderer.addSeriesRenderer(r);

        return renderer;
    }

    private static void setChartSettings(XYMultipleSeriesRenderer renderer,
            double xMin, double xMax, double yMin, double yMax) {
        renderer.setChartTitle("各类型消费统计图");
        renderer.setXTitle("类型");
        renderer.setXLabels(0);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        // 设置坐标的颜色
        renderer.setLabelsColor(Color.parseColor("#eeba06"));
        renderer.setAxesColor(Color.parseColor("#434343"));
        renderer.setXLabelsColor(Color.parseColor("#434343"));
        renderer.setYLabelsColor(0, Color.parseColor("#434343"));
    }

    /*************** ##pragma -- Line Chart Method Area ****************************/
    /**
     * Builds an XY multiple series renderer.
     * 
     * @param colors the series rendering colors
     * @param styles the series point styles
     * @return the XY multiple series renderers
     */
    protected static XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    protected static void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
            PointStyle[] styles) {
        renderer.setAxisTitleTextSize(22);
        renderer.setChartTitleTextSize(22);
        renderer.setLabelsTextSize(22);
        renderer.setLegendTextSize(22);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] {
                40, 60, 55, 10
        });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    /**
     * Sets a few of the series renderer settings.
     * 
     * @param renderer the renderer to set the properties to
     * @param title the chart title
     * @param xTitle the title for the X axis
     * @param yTitle the title for the Y axis
     * @param xMin the minimum value on the X axis
     * @param xMax the maximum value on the X axis
     * @param yMin the minimum value on the Y axis
     * @param yMax the maximum value on the Y axis
     * @param axesColor the axes color
     * @param labelsColor the labels color
     */
    protected static void setChartSettings(XYMultipleSeriesRenderer renderer, String title,
            String xTitle,
            String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
            int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(Color.parseColor("#434343"));
        renderer.setLabelsColor(Color.parseColor("#04d504"));
        renderer.setXLabelsColor(labelsColor);
        renderer.setYLabelsColor(0, labelsColor);
    }

    /**
     * Builds an XY multiple dataset using the provided values.
     * 
     * @param titles the series titles
     * @param xValues the values for the X axis
     * @param yValues the values for the Y axis
     * @return the XY multiple dataset
     */
    protected static XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
            List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    public static void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
            List<double[]> xValues,
            List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    protected static DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
}
