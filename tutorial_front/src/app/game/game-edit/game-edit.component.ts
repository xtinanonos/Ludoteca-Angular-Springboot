import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthorService } from 'src/app/author/author.service';
import { Author } from 'src/app/author/model/Author';
import { CategoryService } from 'src/app/category/category.service';
import { Category } from 'src/app/category/model/Category';
import { GameService } from '../game.service';
import { Game } from '../model/Game';

@Component({
    selector: 'app-game-edit',
    templateUrl: './game-edit.component.html',
    styleUrls: ['./game-edit.component.scss']
})
export class GameEditComponent implements OnInit {

    game: Game;   // objeto juego que estamos editando
    authors: Author[];  // lista de autores
    categories: Category[]; // lista de categorias

    constructor(
        public dialogRef: MatDialogRef<GameEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,      // obtiene datos enviados al abrir el dialogo
        private gameService: GameService,
        private categoryService: CategoryService,
        private authorService: AuthorService,
    ) { }

    ngOnInit(): void {
        if (this.data.game != null) {       // si se pulsa editar
            this.game = Object.assign({}, this.data.game);    // se asignan los datos del juego enviado
        } 
        else {                              // si se pulsa nuevo
            this.game = new Game();         // se crea un nuevo juego
        }

        this.categoryService.getCategories().subscribe(
            categories => {
                this.categories = categories;

                   // si el juego tiene categoria asignada, la selecciona de la lista automaticamente
                if (this.game.category != null) {  
                  //.filter es una funcion java q crea un array con los elementos q cumplen una funcion determinada
                    let categoryFilter: Category[] = categories.filter(category => category.id == this.data.game.category.id);
                    if (categoryFilter != null) {
                        this.game.category = categoryFilter[0]; // le asigna al objeto la categoria recibida
                    }
                }
            }
        );

        this.authorService.getAllAuthors().subscribe(
            authors => {
                this.authors = authors

                if (this.game.author != null) {
                    let authorFilter: Author[] = authors.filter(author => author.id == this.data.game.author.id);
                    if (authorFilter != null) {
                        this.game.author = authorFilter[0];
                    }
                }
            }
        );
    }

    onSave() {
        console.log(this.game);
        this.gameService.saveGame(this.game).subscribe(result => {
            this.dialogRef.close();
        });    
    }  

    onClose() {
        this.dialogRef.close();
    }

}
