import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { Client } from '../model/Client';

@Component({
  selector: 'app-client-edit',
  templateUrl: './client-edit.component.html',
  styleUrls: ['./client-edit.component.scss']
})
export class ClientEditComponent implements OnInit {

  client : Client;
  errorMessage: string;

  constructor(
    public dialogRef: MatDialogRef<ClientEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private clientService: ClientService
  ) { }

  ngOnInit(): void {
    if (this.data.client != null) {
      this.client = Object.assign({}, this.data.client);  // se hace una copia del objeto para q no se modifique el objeto del listado
    }
    else {
      this.client = new Client();
    }
  }

    
    onSave() {
      this.clientService.saveClient(this.client).subscribe({
        next: (result) => {
          this.dialogRef.close(); // Cierra el diálogo si se guarda correctamente
        },
        error: (err) => {   // si al intentar guardar el cliente devuelve un error:
          
            this.errorMessage = "El cliente ya existe.";
          
        }
      });
    }

  onClose() {
    this.dialogRef.close();
  }

}

