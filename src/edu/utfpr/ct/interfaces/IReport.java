package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.gamecontroller.Table;

public interface IReport
{

	/**
	 * Cria um relatório a partir de um jogo.
	 *
	 * @param game Jogo que será feito o relatório.
	 * @return Verdadeiro se tiver havido sucesso, falso do contrário.
	 */
	public boolean createReport(Table table);

	/**
	 * Excluí definitivamente um relatório.
	 * A operação não pode ser desfeita.
	 *
	 * @param game Jogo a ser destruído.
	 * @return Verdadeiro se o relatório foi completamente destruído, falso do contrário.
	 */
	public boolean purgeReport(Game game);

	/**
	 * Retorna a lista de relatórios existentes, no formato de Game.
	 *
	 * @return ista de relatórios.
	 */
	public Game[] getReports();
}
