import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { LoanService } from '../loan.service';
import { Loan } from '../model/Loan';
import { Client } from 'src/app/client/model/Client';
import { Game } from 'src/app/game/model/Game';
import { GameService } from 'src/app/game/game.service';
import { ClientService } from 'src/app/client/client.service';
import { FormattedLoan } from '../model/FormattedLoan';
import { formatDate } from '@angular/common';

@Component({
selector: 'app-loan-edit',
templateUrl: './loan-edit.component.html',
styleUrls: ['./loan-edit.component.scss']
})
export class LoanEditComponent implements OnInit {

    loan : Loan;
    clients : Client[];
    games : Game[];
    errorMessage: string = '';
    dateFormat = 'dd.MM.yyyy';
    formattedLoan: any = {};

    constructor(
        public dialogRef: MatDialogRef<LoanEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private loanService: LoanService,
        private gameService: GameService,
        private clientService: ClientService,
    ) { }

    ngOnInit(): void {
       
            this.loan = new Loan();

            this.gameService.getGames().subscribe(
                games => this.games = games
            );
    
            this.clientService.getClients().subscribe(
                clients => this.clients = clients
            );
  }


onSave() {
    // Formateo de fechas
    const formatted_date_start = formatDate(this.loan.date_start, 'dd.MM.yyyy', 'en-US');
    const formatted_date_end = formatDate(this.loan.date_end, 'dd.MM.yyyy', 'en-US');

    // Creacion de objeto FormattedLoan (con las fechas como string)
    const formattedLoan: FormattedLoan = {
        id: this.loan.id,
        game: this.loan.game,
        client: this.loan.client,
        date_start: formatted_date_start,
        date_end: formatted_date_end
    };

    this.loanService.saveLoan(formattedLoan).subscribe({
        next: (result) => {
            this.dialogRef.close(result);
        },
        error: (err) => {
            let errorMessage = err.error || 'Error al guardar el préstamo';
            
            // Eliminar la coma del mensaje de error
            errorMessage = errorMessage.replace(/,/g, '');  
    
            this.errorMessage = errorMessage;
        }
    });
}

    onClose() {
        this.dialogRef.close();
    }

}
