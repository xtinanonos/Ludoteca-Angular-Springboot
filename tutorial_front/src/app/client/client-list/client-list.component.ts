import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Client } from '../model/Client';
import { MatDialog } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { ClientEditComponent } from '../client-edit/client-edit.component';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.scss']
})
export class ClientListComponent implements OnInit {

  dataSource = new MatTableDataSource<Client>();
  displayedColumns: string[] = ['id', 'name', 'action'];

  constructor(
    private clientService: ClientService,
    public dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.clientService.getClients().subscribe(
      clients => this.dataSource.data = clients   // clients es el dato recibido desde el backend
    );
  }

  createClient() {    
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });    
  }  

  // es lo mismo que el create solo q pasandole un cliente desde el boton del html
  editClient(client: Client) {
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: { client: client }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });
  }

  deleteClient(client: Client) {    
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: { title: "Eliminar cliente", description: "Atención, si borra el cliente se perderán sus datos.<br> ¿Desea eliminar el cliente?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {   // si el dialogo de confirmacion devuelve true 
        this.clientService.deleteClient(client.id).subscribe(result => {  // llama al servicio de eliminar
          this.ngOnInit();
        }); 
      }
    });
   }  

}
