package software.ulpgc.kata4;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;


public class SqliteTrackLoader implements TrackLoader{
    private final Connection connection;

    public SqliteTrackLoader(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Track> loadAll() {
        try {
            return load(queryAll());
        } catch (SQLException e) {
            return emptyList();
        }
    }

    private List<Track> load(ResultSet resultSet) throws SQLException {
        List<Track> list = new ArrayList<>();
        while (resultSet.next())
            list.add(trackFrom(resultSet));
        return list;
    }

    private Track trackFrom(ResultSet resultSet) throws SQLException {
        return new Track(
            resultSet.getString("track"),
            resultSet.getString("composer"),
            resultSet.getInt("milliseconds"),
            resultSet.getDouble("unitPrice"),
            resultSet.getString("album"),
            resultSet.getString("genre"),
            resultSet.getString("artist")
        );
    }

    private static final String QueryAll = "SELECT tracks.name as track, Composer, Milliseconds, UnitPrice, title as album, genres.Name as genre, artists.Name as artist\n" +
            "FROM tracks, albums, genres, artists \n" +
            "WHERE\n" +
            "\talbums.AlbumId = tracks.AlbumId AND\n" +
            "\tgenres.GenreId = tracks.GenreId AND\n" +
            "\tartists.ArtistId = albums.ArtistId";

    private ResultSet queryAll() throws SQLException {
        return connection.createStatement().executeQuery(QueryAll);
    }
}
