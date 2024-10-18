package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * @crbolano ccsw
 *
 */
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @Column(name = "date_start", nullable = false)
    private LocalDate date_start;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @Column(name = "date_end", nullable = false)
    private LocalDate date_end;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id new value of {@link #getId}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * @param game new value of {@link #getGame}
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * @return client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client new value of {@link #getClient}
     */
    public void setClient(Client client) {
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
