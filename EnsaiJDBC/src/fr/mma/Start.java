package fr.mma;

import java.sql.SQLException;

import fr.mma.outils.Connexion;
import fr.mma.outils.DAO;
import fr.mma.outils.LivreDAO;
import fr.mma.outils.SGBD;
import fr.mma.outils.model.Livre;


public class Start {
	
	static String LIVRE_TABLE_QUERY =  "CREATE TABLE LIVRE (ID SERIAL, AUTEUR char(50), NOM char(50), PRIMARY KEY (ID))";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Connexion.connecter();
			System.out.println(SGBD.getTables());
			DAO.executQuery(LIVRE_TABLE_QUERY);
			
			LivreDAO livreDAO = new LivreDAO();
			Livre livre = new Livre();
			livre.setAuteur("Hugo");
			livre.setNom("Miserable");
			livreDAO.create(livre);
			

			Livre livre2 = new Livre();
			livre2.setAuteur("Hugo");
			livre2.setNom("Notre-Dame");
			livreDAO.create(livre2);
			
			livreDAO.delete(livre2);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

