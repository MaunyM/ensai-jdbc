package fr.ensai.outils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class SGBD {

	/**
	 * Cette m�thode vous permet de r�cup�rer les descriptions des tables pr�sentes dans votre base.
	 * @return les diff�rentes lignes de la table pg_tables, constitu�es de 7 valeurs. 
	 * @throws SQLException 
	 */
	public static List<List<Object>> getTables() throws SQLException{
		return getTable("pg_tables",(byte) 7, "WHERE tableowner=\'"+Connexion.ID+"\'") ;
	}

	/**
	 * Cette m�thode vous permet de r�cup�rer les lignes de la table nom pr�sente dans votre base. 
	 * @param nom : le nom de la table � consulter (ex : METIER)
	 * @param taille : le nombre de colonnes � retourner. si taille vaut 3, les 3 premi�res colonnes seront retourn�es. 
	 * @return les diff�rentes lignes de la table nom, constitu�es de taille valeurs.
	 * Dans le cadre de votre projet, vous pourrez utiliser les m�thodes pr�d�finies afin de mieux contr�ler vos types de donn�es 
	 * exemple :  res.getInt(columnIndex);  res.getString(columnIndex);
	 * exemple de requete : SELECT * FROM METIER
	 * @throws SQLException 
	 */
	public static List<List<Object>> getTable(String nom, byte taille) throws SQLException{
		return getTable(nom, taille, "");
	}

	/**
	 * Cette m�thode vous permet de r�cup�rer les lignes de la table nom pr�sente dans votre base. 
	 * @param nom : le nom de la table � consulter (ex : METIER)
	 * @param taille : le nombre de colonnes � retourner. si taille vaut 3, les 3 premi�res colonnes seront retourn�es. 
	 * @param contrainte : une clause WHERE compl�te, avec le mot cl� WHERE, pouvant �tre compl�tement vide
	 * @return les diff�rentes lignes de la table nom, constitu�es de taille valeurs.
	 * Dans le cadre de votre projet, vous pourrez utiliser les m�thodes pr�d�finies afin de mieux contr�ler vos types de donn�es 
	 * exemple :  res.getInt(columnIndex);  res.getString(columnIndex);
	 * exemple de requete : SELECT * FROM METIER WHERE NOM = 'toto'
	 * @throws SQLException
	 */
	public static List<List<Object>> getTable(String nom, byte taille, String contrainte) throws SQLException{
		final byte TAILLE_TABLE = taille;
		List<List<Object>> rep = new ArrayList<List<Object>>();
		Statement st = Connexion.getInstance().createStatement() ;
		// un ResultSet permet de r�cup�rer les diff�rentes lignes obtenues par la requ�te
		String requete= "SELECT * FROM "+nom+" "+contrainte;
		ResultSet res = st.executeQuery(requete) ;
		System.out.println("getTable : requete = "+requete);
		// tant que le ResultSet poss�de des r�ponses, on le lit
		while(res.next()){
			ArrayList<Object> ligne = new ArrayList<Object>(TAILLE_TABLE);
			// connaitre la taille de la table permet d'automatiser la lecture de chaque attribut du ResultSet,
			// pour autant, ici, les types lues ne sont pas contr�l�s (tout est Object)
			for (int i = 1; i <= TAILLE_TABLE; i++) {
				ligne.add(res.getObject(i));
			}
			rep.add(ligne);
		}
		return rep;
	}


	/**
	 * Permet de supprimer la table nomTable de votre base de donn�es. L'utilisation du mot cl� CASCADE vous permettra de
	 * supprimer vos tables en tenant compte de leurs d�pendances.
	 * @param nomTable : la table � supprimer (ex : METIER)
	 * exemple de requete : DROP TABLE METIER
	 * @throws SQLException 
	 */
	public static void supprimerTable(String nomTable) throws SQLException{
		Statement st = Connexion.getInstance().createStatement() ;
		// Lorsque la requ�te ne retourne pas de tuples, on utilise la m�thode de mise � jour.
		st.executeUpdate("DROP TABLE "+nomTable) ;
		System.out.println(nomTable+" supprim�e");
	}


	/**
	 * M�thode permettant d'ajouter une ligne dans votre base de donn�es.
	 * il est pr�f�rable de passer par une m�thode utilisant les PreparedStatement.
	 * @param nomTable : le nom de la table � enrichir ex : METIER
	 * @param attributs : les attributs de cette table ex : [NOM, VILLE]
	 * @param valeurs : une ligne de valeur, dans le m�me ordre que les attributs ex : ['secr�taire', 'Bruz']
	 * exemple de requete : INSERT INTO METIER (NOM, VILLE) VALUES ('secr�taire', 'Bruz')
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
			System.out.println("ins�rerTuple : requete = "+requete);
			PreparedStatement pst=null;
			pst = Connexion.getInstance().prepareStatement(requete);
			/*
			 * La m�thode prepareStatement(requete) peut utiliser une requete acceptant des param�tres.
			 * C'est ce genre de requ�te que vous utiliserez vous.
			 * Regardez la m�thode insererTuple() pr�sent�e ci-dessous.
			 */
			insererTuple(pst);
		}

	}

	/**
	 * Cette m�thode ins�re un tuple en dur, elle vous sert d'exemple pour voir comment peuvent
	 * �tre manipul�s les PreparedStatement
	 * @throws SQLException 
	 */
	public static void insererTuple() throws SQLException {
		PreparedStatement pst = null;
		String requete = "INSERT INTO METIER (NOM, VILLE) VALUES (?, ?)";			
		pst = Connexion.getInstance().prepareStatement(requete);
		pst.setString(1, "un m�tier original");
		pst.setString(2, "L\'yon");

		insererTuple(pst);

	}

	/**
	 * Cette m�thode met en commun le code d'ex�cution du PreparedStatement, avec le bloc try catch ad�quat.
	 * @param pst
	 * @throws SQLException 
	 */
	public static void insererTuple(PreparedStatement pst) throws SQLException{
		pst.executeUpdate() ;
	}


	/**
	 * 
	 * @param nomTable : le nom de la table � modifier ex : METIER
	 * @param modification : la modification � op�rer ex : VILLE = 'Bain' 
	 * @param critere : le crit�re permettant d'identifier la ligne � modifier ex : NOM = 'secr�taire'
	 * exemple de requete : UPDATE METIER SET VILLE = 'Bain' WHERE NOM = 'secr�taire'
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
	 * @param critere : le crit�re permettant d'identifier la ligne � supprimer ex : NOM = 'secr�taire'
	 * exemple de requete : DELETE FROM METIER WHERE NOM = 'secr�taire'
	 * @throws SQLException 
	 */
	public static void supprimerTuple(String nomTable, String critere) throws SQLException{
		String requete = "DELETE FROM "+nomTable+" WHERE "+critere;
		System.out.println("supprimerTuple : requete = "+requete);
		Statement st = Connexion.getInstance().createStatement() ;
		st.executeUpdate(requete) ;
	}	


}