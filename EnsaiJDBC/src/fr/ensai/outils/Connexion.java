package fr.ensai.outils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

	protected static Connection connexion = null ;
	protected static final String ID = "postgres"; //(Login)
	protected static final String MDP = "cachou"; //(Mot de passe)
	// Il est bien �vident que votre mot de passe ne doit pas figurer en clair dans le
	// code d'une programme, ni dans un autre document d'ailleurs.
	// Nous utilisons ce mode de fonctionnement simplifi� qui laisse aux informaticiens
	// les probl�matiques de s�curit�.
	
	protected final static String URL = "jdbc:postgresql://localhost:5432/";

	/**
	 * Pour obtenir une instance de connexion, il faut s'�tre connect�.
	 * @return la connexion actuelle, �ventuellement null.
	 */
	public static Connection getInstance(){
		return connexion;
	}

	/**
	 * Cette m�thode propose un point d'entr�e � votre base de donn�es.
	 * Vous devez avoir indiqu� les bonnes constantes pour ID et MDP
	 * Le driver postgresql indiqu� sur Moodle doit avoir �t� ajout� au path de votre projet
	 * (add external jar)
	 * @throws SQLException 
	 */
	public static void connecter() throws SQLException{
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Driver ok");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (connexion==null) {
			connexion = DriverManager.getConnection(URL, ID, MDP);
			System.out.println("Connexion r�ussie");
		}
	}

	/**
	 * Cette m�thode doit �tre appel�e avant la fermeture de votre programme.
	 * Elle doit m�me �tre appel�e chaque fois que vous avez termin� un traitement sur la base. 
	 * Elle lib�re la connexion � votre Base de Donn�es
	 * La clause finally est particuli�rement pertinente pour appeler cette m�thode.
	 * @throws SQLException 
	 */
	public static void fermer() throws SQLException
	{
		if (connexion!=null) {
			connexion.close();
			connexion=null;
			System.out.println("deconnexion ok");
		}
	}



}