package fr.mma.outils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

	protected static Connection connexion = null ;
	protected static final String ID = "postgres"; //(Login)
	protected static final String MDP = "cachou"; //(Mot de passe)
	// Il est bien évident que votre mot de passe ne doit pas figurer en clair dans le
	// code d'une programme, ni dans un autre document d'ailleurs.
	// Nous utilisons ce mode de fonctionnement simplifié qui laisse aux informaticiens
	// les problématiques de sécurité.
	
	protected final static String URL = "jdbc:postgresql://localhost:5432/";

	/**
	 * Pour obtenir une instance de connexion, il faut s'être connecté.
	 * @return la connexion actuelle, éventuellement null.
	 */
	public static Connection getInstance(){
		return connexion;
	}

	/**
	 * Cette méthode propose un point d'entrée à votre base de données.
	 * Vous devez avoir indiqué les bonnes constantes pour ID et MDP
	 * Le driver postgresql indiqué sur Moodle doit avoir été ajouté au path de votre projet
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
			System.out.println("Connexion réussie");
		}
	}

	/**
	 * Cette méthode doit être appelée avant la fermeture de votre programme.
	 * Elle doit même être appelée chaque fois que vous avez terminé un traitement sur la base. 
	 * Elle libère la connexion à votre Base de Données
	 * La clause finally est particulièrement pertinente pour appeler cette méthode.
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