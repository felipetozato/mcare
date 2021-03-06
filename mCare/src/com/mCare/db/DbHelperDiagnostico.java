package com.mCare.db;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mCare.consulta.Consulta;
import com.mCare.diagnostico.Diagnostico;
import com.mCare.medicamento.Medicamento;


public class DbHelperDiagnostico {

	public Db dbhelper;
	
	public DbHelperDiagnostico(Context context){
		dbhelper = Db.getInstance(context);
	}
	
	public long insereDiagnostico(Diagnostico d){
		ContentValues cv = new ContentValues();
		
		cv.put("nome", d.getNome());
		
		long id = dbhelper.insert(dbhelper.TABLE_NAME_DIAGNOSTICO, cv);
		
		return id;
	}
	
	public boolean deletaDiagnostico(long id){
		
		int i = dbhelper.delete(dbhelper.TABLE_NAME_DIAGNOSTICO, "id_Diagnostico=?", new String[]{""+id});
		
		if(i > 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Diagnostico buscaDiagnostico(int id) {

		String query = "SELECT id_diagnostico, nome "
				+ " FROM " + dbhelper.TABLE_NAME_DIAGNOSTICO
				+ " WHERE id_diagnostico = '" + id + "';";

		Cursor c = dbhelper.exercutaSELECTSQL(query, null);
		Diagnostico d = null;

		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {

				//Pega do select
				int idDiagnostico = c.getInt(0);
				String nomeDiagnostico = c.getString(1);
				
				//Coloca na classe
				d = new Diagnostico(idDiagnostico,nomeDiagnostico);
				
				c.moveToNext();
			}
		}
		return d;
	}

	public ArrayList<Diagnostico> listaDiagnosticos(){
		
		//DEFINE QUERY dependendo se quer favoritos ou nao
		String query = " SELECT id_diagnostico, nome " +
						" FROM "+dbhelper.TABLE_NAME_DIAGNOSTICO;
		
		//Executa o SQL
		Cursor cursor = dbhelper.exercutaSELECTSQL(query, null);
		
		Log.i("SQL", "cursor esta fechado? : " + cursor.isFirst());
		if(cursor.moveToFirst()){
			Log.i("SQL","cursor possui linhas");
			
			//Armazena resultado
			ArrayList<Diagnostico> listaDiagnosticos = new ArrayList<Diagnostico>();
			while(!cursor.isAfterLast()){
				Log.i("SQL","passou no is afterlast");
				
				int id = Integer.parseInt(cursor.getString(0));
				String nome = cursor.getString(1);
				
				Diagnostico d = new Diagnostico(id, nome);
				listaDiagnosticos.add(d);
				cursor.moveToNext();
			}
			Collections.sort(listaDiagnosticos, new Comparator<Diagnostico>() {
		         @Override
		         public int compare(Diagnostico o1, Diagnostico o2) {
		             return Collator.getInstance().compare(o1.getNome(), o2.getNome());
		         }
		     });
			return listaDiagnosticos;
		}
		else{
			return null;
		}
	}
	
	public ArrayList<Diagnostico> listaDiagnosticos(Consulta c){
	
		//Busca todos os diagnosticos feitos naquela consulta
		String query = " SELECT diagnostico.id_diagnostico, diagnostico.nome, diagnostico_consulta.id_consulta, diagnostico_consulta.data_consulta" +
						" FROM diagnostico " +
						" INNER JOIN diagnostico_consulta ON diagnostico.id_diagnostico = diagnostico_consulta.id_diagnostico " +
						" WHERE diagnostico_consulta.id_consulta = " + c.getId();
		
		//Executa o SQL
		Cursor cursor = dbhelper.exercutaSELECTSQL(query, null);
		
		Log.i("SQL", "cursor esta fechado? : " + cursor.isFirst());
		if(cursor.moveToFirst()){
			Log.i("SQL","cursor possui linhas");
			
			//Armazena resultado
			ArrayList<Diagnostico> listaDiagnosticos = new ArrayList<Diagnostico>();
			while(!cursor.isAfterLast()){
				Log.i("SQL","passou no is afterlast");
				
				int id_diagnostico = Integer.parseInt(cursor.getString(0));
				String nome = cursor.getString(1);
				int id_consulta = Integer.parseInt(cursor.getString(2));
				GregorianCalendar hora = dbhelper.textToGregorianCalendar(cursor.getString(3));
				
				Diagnostico d = new Diagnostico(id_diagnostico, nome);
				d.setIdConsulta(id_consulta);
				d.setHora(hora);
				
				//Adiciona aos diagnosticos
				listaDiagnosticos.add(d);
				cursor.moveToNext();
			}
			
			//Ordena diagnosticos pelo nome
			Collections.sort(listaDiagnosticos, new Comparator<Diagnostico>() {
		         @Override
		         public int compare(Diagnostico o1, Diagnostico o2) {
		             return Collator.getInstance().compare(o1.getNome(), o2.getNome());
		         }
		     });
			
			return listaDiagnosticos;
		}
		return null;
	}
}

