package fr.mma.outils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class DAO<T> {
	public Connection connect = Connexion.getInstance();

	/**
	 * Permet de récupérer un objet via son ID
	 * 
	 * @param id
	 * @return
	 */
	public abstract T find(long id);

	/**
	 * Permet de créer une entrée dans la base de données par rapport à un objet
	 * 
	 * @param obj
	 */
	public abstract T create(T obj);

	/**
	 * Permet de mettre à jour les données d'une entrée dans la base
	 * 
	 * @param obj
	 */
	public abstract T update(T obj);

	/**
	 * Permet la suppression d'une entrée de la base
	 * 
	 * @param obj
	 */
	public abstract void delete(T obj);

	protected abstract T build(ResultSet resultSet);

	protected List<T> buildList(ResultSet resultSet) {
		List<T> list = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				list.add(build(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Probleme lors du parcours du resultSet");
		}
		return list;
	}

	public static boolean executQuery(String query) {
		Statement statement;
		try {
			statement = Connexion.getInstance().createStatement();
			return statement.execute(query);
		} catch (SQLException e) {
			System.out.println("Probleme lors de la l'execution de la requete "
					+ query);
		}
		return false;
	}
}