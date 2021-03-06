package com.mCare.db;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import com.mCare.consulta.Consulta;
import com.mCare.paciente.Paciente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DbHelperPaciente {

	private Db dbhelper;

	public DbHelperPaciente(Context context) {
		dbhelper = Db.getInstance(context);

	}

	public boolean updatePaciente(Paciente p){
		
		boolean deucerto;
		
		ContentValues cv = new ContentValues();

		cv.put("nome", p.getNome());
		GregorianCalendar gc = p.getDataNascimento();
		cv.put("data_nascimento", dbhelper.formataData(gc));
		cv.put("sexo", p.getSexo());
		cv.put("escolaridade", p.getEscolaridade());
		cv.put("parente", p.getParente());
		cv.put("parente_tel", p.getParente_tel());
		cv.put("parente_cel", p.getParente_cel());
		cv.put("logradouro", p.getLogradouro());
		cv.put("bairro", p.getBairro());
		cv.put("numero", p.getNumero());
		cv.put("tipo_end", p.getTipo_endereco());
		cv.put("cep", p.getCep());
		cv.put("cidade", p.getCidade());
		cv.put("complemento", p.getComplemento());
		
		deucerto = dbhelper.update(dbhelper.TABLE_NAME_PACIENTES, cv, "id_paciente = "+p.getBd_id(), null);
		
		if(!deucerto){
			return deucerto;
		}
		
		DbHelperTelefone db = new DbHelperTelefone(dbhelper.getContext());
		if(p.getTelefone() != null){
			deucerto = db.updateTelefone((long) p.getBd_id(), p.getTelefone(), p.getTipo_tel(), "id_telefone=?", new String[]{""+p.getIdTel1()});
		}
		if(p.getTel2() != null){
			deucerto = db.updateTelefone((long) p.getBd_id(), p.getTel2(), p.getTipo_tel2(), "id_telefone=?", new String[]{""+p.getIdTel2()});
		}
		if(p.getTel3() != null){
			deucerto =  db.updateTelefone((long) p.getBd_id(), p.getTel3(), p.getTipo_tel3(), "id_telefone=?", new String[]{""+p.getIdTel3()}); 
		}
		
		return deucerto;
	}
	
	public long inserePaciente(Paciente p) {
		long deucerto;

		ContentValues cv = new ContentValues();

		cv.put("nome", p.getNome());
		GregorianCalendar gc = p.getDataNascimento();
		cv.put("data_nascimento", dbhelper.formataData(gc));
		cv.put("sexo", p.getSexo());
		cv.put("escolaridade", p.getEscolaridade());
		cv.put("parente", p.getParente());
		cv.put("parente_tel", p.getParente_tel());
		cv.put("parente_cel", p.getParente_cel());
		cv.put("logradouro", p.getLogradouro());
		cv.put("bairro", p.getBairro());
		cv.put("numero", p.getNumero());
		cv.put("tipo_end", p.getTipo_endereco());
		cv.put("cep", p.getCep());
		cv.put("cidade", p.getCidade());
		cv.put("complemento", p.getComplemento());

		deucerto = dbhelper.insert(dbhelper.TABLE_NAME_PACIENTES, cv);
		return deucerto;
	}

	public int deletaPaciente(Paciente p) {

		int deuCerto = dbhelper.delete(dbhelper.TABLE_NAME_PACIENTES,
				"id_paciente=?", new String[] { "" + p.getBd_id() });

		return deuCerto;
	}

	/**
	 * Obs: os objetos retornados por esse m�todo possuem somente nome e id para
	 * otimizacao de memoria na listagem de todos os pacientes. Para exibir mais
	 * detalhes e necessario nova busca para pegar os detalhes.
	 * 
	 * @return lista de pacientes
	 */
	public ArrayList<Paciente> listaPacientes() {

		Cursor cursor = dbhelper.serach(false, dbhelper.TABLE_NAME_PACIENTES,
				new String[] { "id_paciente", "nome" }, null, null, null, null,
				"nome", null);

		Log.i("SQL", "cursor esta fechado? : " + cursor.isFirst());

		if (cursor.moveToFirst()) {
			Log.i("SQL", "cursor possui linhas");
			ArrayList<Paciente> listaPaciente = new ArrayList<Paciente>(
					cursor.getCount());

			while (!cursor.isAfterLast()) {
				Log.i("SQL", "passou no is afterlast");
				int id = cursor.getInt(0);
				String nome = cursor.getString(1);

				Paciente p = new Paciente(id, nome);

				listaPaciente.add(p);
				cursor.moveToNext();
			}
			return listaPaciente;
		} else {
			return null;
		}
	}

	public Paciente buscaPaciente(int id) {

		String query = "SELECT * FROM " + dbhelper.TABLE_NAME_PACIENTES
				+ " WHERE id_paciente = " + id + ";";

		Cursor c = dbhelper.exercutaSELECTSQL(query, null);
		
		Paciente p = null;

		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {

				int idpaciente = c.getInt(0);
				String nome = c.getString(1);
				GregorianCalendar gc = dbhelper.textToGregorianCalendar(c
						.getString(2));
				byte sexo = (byte) c.getInt(3);
				String escolaridade = c.getString(4);
				String parente = c.getString(5);
				String parente_tel = c.getString(6);
				String parente_cel = c.getString(7);
				String logradouro = c.getString(8);
				String bairro = c.getString(9);
				int numero = c.getInt(10);
				String tipo_end = c.getString(11);
				String cep = c.getString(12);
				String cidade = c.getString(13);
				String complemento = c.getString(14);

				p = new Paciente(idpaciente, nome, gc, sexo, logradouro,bairro, numero, cidade);

				p.setEscolaridade(escolaridade);
				p.setParente(parente);
				p.setParente_tel(parente_tel);
				p.setParente_cel(parente_cel);
				p.setTipo_endereco(tipo_end);
				p.setCep(cep);
				p.setComplemento(complemento);
				c.moveToNext();
			}
			
		}
		Cursor ct = dbhelper.serach(false, dbhelper.TABLE_NAME_TELEFONE, new String[]{"telefone","tipo_tel","id_telefone"}, "fk_paciente=?",new String[]{""+p.getBd_id()},null, null, null,null);
		
		if(ct.moveToFirst()){
			int nrow = ct.getCount();
			
			Log.i("inf","numero de linhas encontradas"+nrow);
			
			if(nrow == 1){
				p.setTelefone(ct.getString(0));
				p.setTipo_tel(ct.getString(1));
				p.setIdTel1(ct.getLong(2));
			}else if(nrow == 2){
				p.setTelefone(ct.getString(0));
				p.setTipo_tel(ct.getString(1));
				p.setIdTel1(ct.getLong(2));
				ct.moveToNext();
				p.setTel2(ct.getString(0));
				p.setTipo_tel2(ct.getString(1));
				p.setIdTel2(ct.getLong(2));
			}else if(nrow == 3){
				p.setTelefone(ct.getString(0));
				p.setTipo_tel(ct.getString(1));
				p.setIdTel1(ct.getLong(2));
				ct.moveToNext();
				p.setTel2(ct.getString(0));
				p.setTipo_tel2(ct.getString(1));
				p.setIdTel2(ct.getLong(2));
				ct.moveToNext();
				p.setTel3(ct.getString(0));
				p.setTipo_tel3(ct.getString(1));
				p.setIdTel3(ct.getLong(2));
			}
		}
		
		return p;
	}

	public Paciente buscaPaciente(String nome) {

		String query = "SELECT * FROM " + dbhelper.TABLE_NAME_PACIENTES
				+ " WHERE nome = '" + nome + "';";

		Cursor c = dbhelper.exercutaSELECTSQL(query, null);
		Paciente p = null;

		if (c.moveToFirst()) {
			while (!c.isAfterLast()) {

				int idpaciente = c.getInt(0);
				GregorianCalendar gc = dbhelper.textToGregorianCalendar(c.getString(2));
				byte sexo = (byte) c.getInt(3);
				String escolaridade = c.getString(4);
				String parente = c.getString(5);
				String parente_tel = c.getString(6);
				String parente_cel = c.getString(7);
				String logradouro = c.getString(8);
				String bairro = c.getString(9);
				int numero = c.getInt(10);
				String tipo_end = c.getString(11);
				String cep = c.getString(12);
				String cidade = c.getString(13);
				String complemento = c.getString(14);

				p = new Paciente(idpaciente, nome, gc, sexo, logradouro,
						bairro, numero, cidade);

				p.setEscolaridade(escolaridade);
				p.setParente(parente);
				p.setParente_tel(parente_tel);
				p.setParente_cel(parente_cel);
				p.setTipo_endereco(tipo_end);
				p.setCep(cep);
				p.setComplemento(complemento);
				c.moveToNext();
			}

			
		}
		return p;
	}
}

	
