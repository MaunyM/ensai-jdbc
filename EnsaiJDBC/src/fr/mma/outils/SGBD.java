package fr.mma.outils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class SGBD {

	/**
	 * Cette méthode vous permet de récupérer les descriptions des tables présentes dans votre base.
	 * @return les différentes lignes de la table pg_tables, constituées de 7 valeurs. 
	 * @throws SQLException 
	 */
	public static List<List<Object>> getTables() throws SQLException{
		return getTable("pg_tables",(byte) 7, "WHERE tableowner=\'"+Connexion.ID+"\'") ;
	}

	/**
	 * Cette méthode vous permet de récupérer les lignes de la table nom présente dans votre base. 
	 * @param nom : le nom de la table à consulter (ex : METIER)
	 * @param taille : le nombre de colonnes à retourner. si taille vaut 3, les 3 premières colonnes seront retournées. 
	 * @return les différentes lignes de la table nom, constituées de taille valeurs.
	 * Dans le cadre de votre projet, vous pourrez utiliser les méthodes prédéfinies afin de mieux contrôler vos types de données 
	 * exemple :  res.getInt(columnIndex);  res.getString(columnIndex);
	 * exemple de requete : SELECT * FROM METIER
	 * @throws SQLException 
	 */
	public static List<List<Object>> getTable(String nom, byte taille) throws SQLException{
		return getTable(nom, taille, "");
	}

	/**
	 * Cette méthode vous permet de récupérer les lignes de la table nom présente dans votre base. 
	 * @param nom : le nom de la table à consulter (ex : METIER)
	 * @param taille : le nombre de colonnes à retourner. si taille vaut 3, les 3 premières colonnes seront retournées. 
	 * @param contrainte : une clause WHERE complète, avec le mot clé WHERE, pouvant être complètement vide
	 * @return les différentes lignes de la table nom, constituées de taille valeurs.
	 * Dans le cadre de votre projet, vous pourrez utiliser les méthodes prédéfinies afin de mieux contrôler vos types de données 
	 * exemple :  res.getInt(columnIndex);  res.getString(columnIndex);
	 * exemple de requete : SELECT * FROM METIER WHERE NOM = 'toto'
	 * @throws SQLException
	 */
	public static List<List<Object>> getTable(String nom, byte taille, String contrainte) throws SQLException{
		final byte TAILLE_TABLE = taille;
		List<List<Object>> rep = new ArrayList<List<Object>>();
		Statement st = Connexion.getInstance().createStatement() ;
		// un ResultSet permet de récupérer les différentes lignes obtenues par la requête
		String requete= "SELECT * FROM "+nom+" "+contrainte;
		ResultSet res = st.executeQuery(requete) ;
		System.out.println("getTable : requete = "+requete);
		// tant que le ResultSet possède des réponses, on le lit
		while(res.next()){
			ArrayList<Object> ligne = new ArrayList<Object>(TAILLE_TABLE);
			// connaitre la taille de la table permet d'automatiser la lecture de chaque attribut du ResultSet,
			// pour autant, ici, les types lues ne sont pas contrôlés (tout est Object)
			for (int i = 1; i <= TAILLE_TABLE; i++) {
				ligne.add(res.getObject(i));
			}
			rep.add(ligne);
		}
		return rep;
	}


	/**
	 * Permet de supprimer la table nomTable de votre base de données. L'utilisation du mot clé CASCADE vous permettra de
	 * supprimer vos tables en tenant compte de leurs dépendances.
	 * @param nomTable : la table à supprimer (ex : METIER)
	 * exemple de requete : DROP TABLE METIER
	 * @throws SQLException 
	 */
	public static void supprimerTable(String nomTable) throws SQLException{
		Statement st = Connexion.getInstance().createStatement() ;
		// Lorsque la requête ne retourne pas de tuples, on utilise la méthode de mise à jour.
		st.executeUpdate("DROP TABLE "+nomTable) ;
		System.out.println(nomTable+" supprimée");
	}


	/**
	 * Méthode permettant d'ajouter une ligne dans votre base de données.
	 * il est préférable de passer par une méthode utilisant les PreparedStatement.
	 * @param nomTable : le nom de la table à enrichir ex : METIER
	 * @param attributs : les attributs de cette table ex : [NOM, VILLE]
	 * @param valeurs : une ligne de valeur, dans le même ordre que les attributs ex : ['secrétaire', 'Bruz']
	 * exemple de requete : INSERT INTO METIER (NOM, VILLE) VALUES ('secrétaire', 'Bruz')
	 * @throws SQLException 
	 */
	public static void insererTuple(String nomTable, ArrayList<String> attributs, ArrayList<String> valeurs) throws SQLException{
		if (attributs.size()==valeurs.size()) {

			String requete = "INSERT INTO "+nomTable+" (";
			for (int i=0; i<attributs.size(); i++) {
				String unAttribut = attributs.get(i);
				requete+=unAttribut;
				if (i+1<attributs.size()) {
					requete+=", ";
				}
			}
			requete+=") VALUES (";
			for (int i=0; i<valeurs.size(); i++) {
				String uneValeur = valeurs.get(i);
				requete+=uneValeur;
				if (i+1<valeurs.size()) {
					requete+=", ";
				}
			}
			requete+=")";
			System.out.println("insérerTuple : requete = "+requete);
			PreparedStatement pst=null;
			pst = Connexion.getInstance().prepareStatement(requete);
			/*
			 * La méthode prepareStatement(requete) peut utiliser une requete acceptant des paramètres.
			 * C'est ce genre de requête que vous utiliserez vous.
			 * Regardez la méthode insererTuple() présentée ci-dessous.
			 */
			insererTuple(pst);
		}

	}

	/**
	 * Cette méthode insère un tuple en dur, elle vous sert d'exemple pour voir comment peuvent
	 * être manipulés les PreparedStatement
	 * @throws SQLException 
	 */
	public static void insererTuple() throws SQLException {
		PreparedStatement pst = null;
		String requete = "INSERT INTO METIER (NOM, VILLE) VALUES (?, ?)";			
		pst = Connexion.getInstance().prepareStatement(requete);
		pst.setString(1, "un métier original");
		pst.setString(2, "L\'yon");

		insererTuple(pst);

	}

	/**
	 * Cette méthode met en commun le code d'exécution du PreparedStatement, avec le bloc try catch adéquat.
	 * @param pst
	 * @throws SQLException 
	 */
	public static void insererTuple(PreparedStatement pst) throws SQLException{
		pst.executeUpdate() ;
	}


	/**
	 * 
	 * @param nomTable : le nom de la table à modifier ex : METIER
	 * @param modification : la modification à opérer ex : VILLE = 'Bain' 
	 * @param critere : le critère permettant d'identifier la ligne à modifier ex : NOM = 'secrétaire'
	 * exemple de requete : UPDATE METIER SET VILLE = 'Bain' WHERE NOM = 'secrétaire'
	 * @throws SQLException 
	 */
	public static void modifierTuple(String nomTable, String modification, String critere) throws SQLException{
		String requete = "UPDATE "+nomTable+" SET "+modification+" WHERE "+critere;
		System.out.println("modificationTuple : requete = "+requete);
		Statement st = Connexion.getInstance().createStatement() ;
		st.executeUpdate(requete) ;
	}	


	/**
	 * 
	 * @param nomTable : le nom de la table qui va perdre une ligne (ex : METIER)
	 * @param critere : le critère permettant d'identifier la ligne à supprimer ex : NOM = 'secrétaire'
	 * exemple de requete : DELETE FROM METIER WHERE NOM = 'secrétaire'
	 * @throws SQLException 
	 */
	public static void supprimerTuple(String nomTable, String critere) throws SQLException{
		String requete = "DELETE FROM "+nomTable+" WHERE "+critere;
		System.out.println("supprimerTuple : requete = "+requete);
		Statement st = Connexion.getInstance().createStatement() ;
		st.executeUpdate(requete) ;
	}	


}