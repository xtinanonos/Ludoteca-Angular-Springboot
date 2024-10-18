import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';
import { Pageable } from 'src/app/core/model/page/Pageable';
import { Loan } from '../model/Loan';
import { LoanService } from '../loan.service';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { Game } from 'src/app/game/model/Game';
import { Client } from 'src/app/client/model/Client';
import { GameService } from 'src/app/game/game.service';
import { LoanSearchDto } from '../model/LoanSearchDto';
import { ClientService } from 'src/app/client/client.service';


@Component({
selector: 'app-loan-list',
templateUrl: './loan-list.component.html',
styleUrls: ['./loan-list.component.scss']
})
export class LoanListComponent implements OnInit {

    pageNumber: number = 0;
    pageSize: number = 5;
    totalElements: number = 0;

    dataSource = new MatTableDataSource<Loan>();
    displayedColumns: string[] = ['id', 'game_name', 'client_name', 'start_date', 'end_date', 'action'];

    games : Game[];
    clients: Client[];
    loans : Loan[];
    filterGame: Game;
    filterClient: Client;
    pickerDate: Date;

    constructor(
        private loanService: LoanService,
        private gameService: GameService,
        private clientService: ClientService,
        public dialog: MatDialog,
        
    ) { }

    ngOnInit(): void {
        // esto es para cargar los datos de los mat-selects
        this.gameService.getGames().subscribe(
            games => this.games = games
        );

        this.clientService.getClients().subscribe(
            clients => this.clients = clients
        );

        this.loadPage();
    }

    // metodo q construye objeto pageable con los valores actuales del componente paginador,
    // comprueba si se ha seleccionado algun filtro y lanza la peticion con esos datos
    loadPage(event?: PageEvent) {
        let pageableRequest = {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
          };
      
          if (event) {
            pageableRequest.pageNumber = event.pageIndex;
            pageableRequest.pageSize = event.pageSize;
          }

          // crea el objeto con el paginado y los filtros
          const searchDto: LoanSearchDto = {
            gameName: this.filterGame!=null ? this.filterGame.title : null,
            idClient: this.filterClient != null ? this.filterClient.id : null,
            date: this.pickerDate ? this.pickerDate.toISOString().split('T')[0] : null, // Convertir a formato ISO 
            pageable: pageableRequest,
          };
    
          // obtiene los préstamos y paginación del service
        this.loanService.getLoans(searchDto).subscribe(data => {
            this.dataSource.data = data.content;
            this.pageNumber = data.pageable.pageNumber;
            this.pageSize = data.pageable.pageSize;
            this.totalElements = data.totalElements;
        });

    }  


    onCleanFilter(): void {
        this.filterGame = null;
        this.filterClient = null;
        this.pickerDate = null;

        this.onSearch();
    }

    onSearch(): void {
        this.loadPage();
    }

    createLoan() {      

        const dialogRef = this.dialog.open(LoanEditComponent, {
            data: {}
        });

        dialogRef.afterClosed().subscribe(result => {
            this.ngOnInit();
        });      
    }  

    deleteLoan(loan: Loan) {    
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
            data: { title: "Eliminar préstamo", description: "Atención, si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?" }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.loanService.deleteLoan(loan.id).subscribe(result =>  {
                    this.ngOnInit();
                }); 
            }
        });
    }  
}


