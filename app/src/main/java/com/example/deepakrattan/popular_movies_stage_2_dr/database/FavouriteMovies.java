package com.example.deepakrattan.popular_movies_stage_2_dr.database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

//Create Table FavouriteMovies

@Entity
public class FavouriteMovies implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int mid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "vote_average")
    private double voteAverage;

    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    public FavouriteMovies(int mid, String title, double voteAverage, String releaseDate, String overview, String posterPath) {
        this.mid = mid;
        this.title = title;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    protected FavouriteMovies(Parcel in) {
        mid = in.readInt();
        title = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        overview = in.readString();
        posterPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mid);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(posterPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavouriteMovies> CREATOR = new Creator<FavouriteMovies>() {
        @Override
        public FavouriteMovies createFromParcel(Parcel in) {
            return new FavouriteMovies(in);
        }

        @Override
        public FavouriteMovies[] newArray(int size) {
            return new FavouriteMovies[size];
        }
    };

    public int getMid() {
        return mid;
    }

    public String getTitle() {
        return title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
