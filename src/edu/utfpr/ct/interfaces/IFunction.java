package edu.utfpr.ct.interfaces;

import java.io.Serializable;

public interface IFunction extends Serializable
{

        /**
         * Posição ocupada pela função em uma cadeia de suprimentos.
         * 
         * Varejista = 1
         * Atacadista = 2
         * Distribuidor = 3
         * Fabricante = 4
         * 
         * @return A posição ocupada pela função.
         */
        public int getPosition();

        /**
         * Retorna a primeira função.
         * @return Primeira função.
         */
        public IFunction first();

        /**
         * Retorna verdadeiro se for a última função da cadeia de suprimentos.
         * Falso do contrário.
         * @return Verdadeiro se for a última função, falso do contrário.
         */
        public boolean isLast();

        /**
         * Retorna a próxima função da cadeia de suprimentos. Null caso seja a última função.
         * 
         * @return Próxima função, ou null se não existir.
         */
        public IFunction next();

        /**
         * Retorna a lista de funções definidas na cadeia de suprimentos.
         * 
         * @return Lista de funções.
         */
        public IFunction[] getValues();

        /**
         * Retorna o nome definido para cada função.
         * 
         * @return O nome da função.
         */
        public String getName();
}
