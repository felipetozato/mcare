package com.mCare;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class VisualizarMedicamento extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizar_medicamento);
		
		//Pega os campos da activity em xml
		TextView medicamento = (TextView) findViewById(R.id.textViewMedicamento);
		TextView tipo = (TextView) findViewById(R.id.textViewTipo);
		TextView dosagem = (TextView) findViewById(R.id.textViewDosagem);
		TextView principioAtivo = (TextView) findViewById(R.id.textViewPrincipioAtivo);
		
		//Pega as informações
		String[] informacoes = (String[]) getIntent().getExtras().get("informacoes");
		
		//Coloca as informações nos campos
		medicamento.setText(informacoes[0]);
		tipo.setText(informacoes[1]);
		dosagem.setText(informacoes[2]);
		principioAtivo.setText(informacoes[3]);
		getActionBar().setTitle("Medicamento:");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizar_medicamento, menu);
		return true;
	}

}
