package com.mCare.paciente.historico;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.mCare.R;
 
/**
 * The simplest possible example of using AndroidPlot to plot some data.
 */
public class HistoricoGrafico extends Activity
{
 
    private XYPlot mySimpleXYPlot;
 
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_grafico);
        
        getActionBar().setTitle("Statistics: " + getIntent().getExtras().getString("nomeCampo").split("@")[0].replace("_", " "));
		getActionBar().setDisplayHomeAsUpEnabled(true);
        
		pegaEstatisticas();
        criaGrafico();
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home: onBackPressed();
		break;
		default: return super.onOptionsItemSelected(item);
		}
		return true;
	}
    
    public void pegaEstatisticas(){
    	
    	double media = getIntent().getExtras().getDouble("media");
    	double desvio = getIntent().getExtras().getDouble("desvio");
    	double maximo = getIntent().getExtras().getDouble("maximo");
    	double minimo = getIntent().getExtras().getDouble("minimo");
    	
    	TextView tMedia = (TextView) findViewById(R.id.textViewMedia);
    	tMedia.setText("Average: " + reduzTamanho(media));
    	TextView tDesvio = (TextView) findViewById(R.id.textViewDesvio);
    	tDesvio.setText("Standard Deviation: " + reduzTamanho(desvio));
    	TextView tMaximo = (TextView) findViewById(R.id.textViewMaximo);
    	tMaximo.setText("Max: " + maximo);
    	TextView tMinimo = (TextView) findViewById(R.id.textViewMinimo);
    	tMinimo.setText("Min: " + minimo);
    }
    
    public String reduzTamanho(double valor){
    	String string = "" + valor;
    	String novoTamanho = "";
    	if(string.length() > 4){
    		for(int i=0; i<4; i++){
    			novoTamanho += string.charAt(i);
    		}
    	}
    	return novoTamanho;
    }
    
    public void criaGrafico(){
    	 // initialize our XYPlot reference:
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        
        ArrayList<String> valores = (ArrayList<String>) getIntent().getExtras().get("valores");
        String nomeCampo = getIntent().getExtras().getString("nomeCampo").split("@")[0].replace("_", " ");
        
        // Create a couple arrays of y-values to plot:
        Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
        
        if(valores!=null){
        	series1Numbers = new Number[valores.size()];
        	for(int i=0; i<series1Numbers.length; i++){
        		series1Numbers[i] = Double.parseDouble(valores.get(i));
        	}
        	Log.i("graph","valores nao e null");
        }
        
        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                nomeCampo);                             // Set the display title of the series
        Log.i("graph","criou o objeto XYSeries");
        // same as above
        //XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
 
        // Create a formatter to use for drawing a series using LineAndPointRenderer:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(255, 255, 0), Color.rgb(0, 100, 0), null, null);
        // fill color (none)
        
        Paint paint = series1Format.getLinePaint();
        paint.setStrokeWidth(13);
        series1Format.setLinePaint(paint);
 
        // add a new series' to the xyplot:
        mySimpleXYPlot.addSeries(series1, series1Format);
        Log.i("graph","passou do add series");
        // same as above:
        //mySimpleXYPlot.addSeries(series2, new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), null, null));
 
        // reduce the number of range labels
       // mySimpleXYPlot.setTicksPerRangeLabel(valores.size());
       // mySimpleXYPlot.setTicksPerDomainLabel(valores.size()/2);
        mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1.0);
        mySimpleXYPlot.setDomainLabel("Consultations");
        mySimpleXYPlot.setRangeBottomMax(getIntent().getExtras().getDouble("minimo")-(int)getIntent().getExtras().getDouble("minimo")/8);
        mySimpleXYPlot.setRangeTopMin(getIntent().getExtras().getDouble("maximo")+(int)getIntent().getExtras().getDouble("maximo")/8);
        mySimpleXYPlot.setRangeStep(XYStepMode.SUBDIVIDE, 16);

    }
}