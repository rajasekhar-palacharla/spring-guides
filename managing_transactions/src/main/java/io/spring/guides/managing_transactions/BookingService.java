package io.spring.guides.managing_transactions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class BookingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public void book(String... persons) {
        for (String person : persons) {
            LOGGER.info("Booking person {} in a seat...", person);
            jdbcTemplate.update("INSERT INTO BOOKINGS(FIRST_NAME) VALUES (?)", person);
        }
    }

    public List<String> findAllBookings() {
        return jdbcTemplate.query("SELECT FIRST_NAME FROM BOOKINGS", (rs, rowNum) -> {
            return rs.getString("FIRST_NAME");
        });
    }
}
