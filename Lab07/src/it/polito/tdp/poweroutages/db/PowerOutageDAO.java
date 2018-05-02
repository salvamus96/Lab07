package it.polito.tdp.poweroutages.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.NercIdMap;
import it.polito.tdp.poweroutages.model.OutageIdMap;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO {

	public List<Nerc> getNercList(NercIdMap nercMap) {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				
				nercList.add(nercMap.get(n));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}

	public void getOutagesFromNerc(Nerc nerc, OutageIdMap outageMap) {
		String sql = "SELECT P.id, P.nerc_id, customers_affected, date_event_began, date_event_finished " + 
					 "FROM poweroutages AS P, nerc AS N " + 
					 "WHERE P.nerc_id = N.id AND N.id = ? " +
					 "ORDER BY date_event_finished";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				PowerOutage p = new PowerOutage (res.getInt("id"),
										res.getInt("nerc_id"), res.getInt("customers_affected"), 
										res.getTimestamp("date_event_began").toLocalDateTime(),
										res.getTimestamp("date_event_finished").toLocalDateTime());

// aggiungo alla lista del NERC selezionato il nuovo oggetto outage creato tramite SQL
				nerc.getOutages().add(outageMap.get(p));

			}
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}


	}
}