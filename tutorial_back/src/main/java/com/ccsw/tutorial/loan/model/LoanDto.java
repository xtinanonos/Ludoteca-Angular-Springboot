package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class LoanDto {

    private Long id;

    private GameDto game;

    private ClientDto client;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date_start;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date_end;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return game
     */
    public GameDto getGame() {
        return game;
    }

    /**
     * @param game new value of {@link #getGame}.
     */
    public void setGame(GameDto game) {
        this.game = game;
    }

    /**
     * @return client
     */
    public ClientDto getClient() {
        return client;
    }

    /**
     * @param client new value of {@link #getClient}.
     */
    public void setClient(ClientDto client) {
        this.client = client;
    }

    /**
     * @return start date of the loan.
     */
    public LocalDate getDate_start() {
        return date_start;
    }

    /**
     * @param date_start new value of {@link #getDate_start}.
     */
    public void setDate_start(LocalDate date_start) {
        this.date_start = date_start;
    }

    /**
     * @return end date of the loan.
     */
    public LocalDate getDate_end() {
        return date_end;
    }

    /**
     * @param date_end new value of {@link #getDate_end}.
     */
    public void setDate_end(LocalDate date_end) {
        this.date_end = date_end;
    }

}
