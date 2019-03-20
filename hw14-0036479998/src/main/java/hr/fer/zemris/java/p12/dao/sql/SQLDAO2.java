package hr.fer.zemris.java.p12.dao.sql;

import hr.fer.zemris.java.p12.dao.DAO2;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.model.OptionEntry;
import hr.fer.zemris.java.p12.model.PollEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 * 
 * @author marcupic
 */
public class SQLDAO2 implements DAO2 {

	@Override
	public List<OptionEntry> getAllPollOptions() throws DAOException {
		List<OptionEntry> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from PollOptions order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						OptionEntry unos = new OptionEntry();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setDescription(rs.getString(3));
						unos.setPollID(rs.getInt(4));
						unosi.add(unos);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.",
					ex);
		}
		return unosi;
	}

	@Override
	public List<OptionEntry> getOptionsByPoll(long id) throws DAOException {
		OptionEntry unos = null;
		Connection con = SQLConnectionProvider.getConnection();
		List<OptionEntry> unosi = new ArrayList<>();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					"select * from PollOptions where pollID=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						unos = new OptionEntry();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setDescription(rs.getString(3));
						unos.setPollID(rs.getInt(4));
						unos.setVotesCount(rs.getInt(5));
						unosi.add(unos);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata korisnika.", ex);
		}

		return unosi;
	}

	@Override
	public OptionEntry getOptionByID(long ID) throws DAOException {
		OptionEntry unos = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from PollOptions where id=?");
			pst.setLong(1, Long.valueOf(ID));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						unos = new OptionEntry();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setDescription(rs.getString(3));
						unos.setPollID(rs.getInt(4));
						unos.setVotesCount(rs.getInt(5));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata korisnika.", ex);
		}
		return unos;
	}

	@Override
	public void updateOptionEntry(OptionEntry newEntry) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("UPDATE PollOptions set votesCount=? WHERE id=?");
			pst.setInt(1, newEntry.getVotesCount());
			pst.setLong(2, newEntry.getId());
			try {
				pst.executeUpdate();
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata korisnika.", ex);
		}
	}

	@Override
	public List<PollEntry> getAllPolls() throws DAOException {
		List<PollEntry> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from Polls order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollEntry unos = new PollEntry();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setMessage(rs.getString(3));
						unosi.add(unos);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.",
					ex);
		}
		return unosi;
	}

	@Override
	public PollEntry getPoll(long pollID) throws DAOException {
		PollEntry unos = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from Polls where id=?");
			pst.setLong(1, Long.valueOf(pollID));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if (rs != null && rs.next()) {
						unos = new PollEntry();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setMessage(rs.getString(3));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata korisnika.", ex);
		}
		return unos;
	}

}