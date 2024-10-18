import { Client } from "src/app/client/model/Client";
import { Game } from "src/app/game/model/Game";

export interface FormattedLoan {
    id: number;
    game: Game;
    client: Client;
    date_start: string;
    date_end: string;
  }