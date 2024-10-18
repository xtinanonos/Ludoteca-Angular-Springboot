import { Injectable } from '@angular/core';
import { catchError, Observable, of, throwError } from 'rxjs';
import { Client } from './model/Client';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(
    private http: HttpClient
  ) { }

  // solicitudes que se lanzan contra el controller del backend a traves de una URL http

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>('http://localhost:8080/client');
  }

  saveClient(client: Client): Observable<Client> {
    let url = 'http://localhost:8080/client';
        if (client.id != null) {
          url += '/'+client.id;
        }
        
        // si ocurre un error, lo captura y se lo pasa al manejador de errores
        return this.http.put<Client>(url, client).pipe(catchError(this.handleError));
  }

  deleteClient(idClient : number): Observable<any> {
    return this.http.delete('http://localhost:8080/client/'+idClient);
  } 


  // Metodo manejador de errores
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Se ha producido un error';  // Mensaje por defecto

    errorMessage = error.message;

    // if (error.error instanceof ErrorEvent) {
    //   // Error del lado del cliente o de la red
    //   errorMessage = `Error en el cliente: ${error.error.message}`;
    // } else {
    //   // Error del servidor
    //   errorMessage = `Error en el servidor: ${error.status} - ${error.message}`;
    // }

    // Log del error en la consola
    console.error(errorMessage);

    return throwError(errorMessage);  // Devuelve el mensaje de error para que lo use el componente
  }
  
}