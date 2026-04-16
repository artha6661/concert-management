package com.edts.concert_management.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edts.concert_management.model.Concert;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
  @Query("select c from Concert c where (coalesce(:q, '') = '' "
      + "or lower(c.name) like concat('%', lower(coalesce(:q, '')), '%') "
      + "or lower(c.venue) like concat('%', lower(coalesce(:q, '')), '%')) "
      + "and (c.startsAt >= coalesce(:startsFrom, c.startsAt)) "
      + "and (c.startsAt <= coalesce(:startsTo, c.startsAt)) "
      + "order by c.startsAt asc")
  List<Concert> search(@Param("q") String q, @Param("startsFrom") Instant startsFrom, @Param("startsTo") Instant startsTo);

}

