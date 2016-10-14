/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.logmanager.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author Allan Vinicius
 */
public class NovoClass
{
	public void asd()
	{
	Connection con = FactoryConnection.getConnection();
		String query = "INSERT INTO avaliacao(id_projeto, id_avaliador, "
						+ "nota_parceiros, nota_atividades, nota_recursos,"
						+ "nota_valor, nota_relacionamentos," 
						+ "nota_canais, nota_segmento, nota_custo, nota_fluxo,"
						+" status, media_final, ultima_alteracao, data_criacao) "
						+"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,(SELECT CURRENT_TIMESTAMP), (SELECT CURRENT_TIMESTAMP)	" ;

		try
		{
			PreparedStatement stmt = con.prepareStatement(query);
			
			/*stmt.setInt(1,idProjeto);
			stmt.setInt(2,idAvaliador);
			stmt.setDouble(3,notaParceiros);
			stmt.setDouble(4,notaAtividades);
			stmt.setDouble(5,notaRecursos);
			stmt.setDouble(6,notaValor);
			stmt.setDouble(7,notaRelacionamentos);
			stmt.setDouble(8,notaCanais);
			stmt.setDouble(9,notaSegmento);
			stmt.setDouble(10,notaCusto);
			stmt.setDouble(11,notaFluxo);
			stmt.setString(12,status);
			stmt.setDouble(13,mediaFinal);
			*/
			stmt.executeQuery();
			stmt.close();
			
		}
		catch(Exception e)
		{
			System.out.println("erro adicionar avaliacao ");
		}
	}
}
