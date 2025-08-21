package com.movie.celestix.features.theater.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public record TheaterResponse(
        Long id,
        String name,
        String location,
        SeatConfigurationData seatConfiguration,
        SeatInfoData premiumSeat,
        SeatInfoData regularSeat,
        SeatInfoData economySeat,
        SeatInfoData basicSeat
) {
    public static final RowMapper<TheaterResponse> MAPPER = (rs, rowNum) -> new TheaterResponse(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("location"),
            new SeatConfigurationData(
                    rs.getInt("seat_row"),
                    rs.getInt("seat_column")
            ),
            new SeatInfoData(
                    rs.getInt("total_premium_seats"),
                    rs.getBigDecimal("total_premium_price")
            ),
            new SeatInfoData(
                    rs.getInt("total_regular_seats"),
                    rs.getBigDecimal("total_regular_price")
            ),
            new SeatInfoData(
                    rs.getInt("total_economy_seats"),
                    rs.getBigDecimal("total_economy_price")
            ),
            new SeatInfoData(
                    rs.getInt("total_basic_seats"),
                    rs.getBigDecimal("total_basic_price")
            )
    );
}
