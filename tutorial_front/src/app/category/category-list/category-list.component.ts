import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Category } from '../model/Category'
import { CategoryService } from '../category.service';
import { MatDialog } from '@angular/material/dialog';
import { CategoryEditComponent } from '../category-edit/category-edit.component';
import { DialogConfirmationComponent } from 'src/app/core/dialog-confirmation/dialog-confirmation.component';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.scss']
})

export class CategoryListComponent implements OnInit {

  dataSource = new MatTableDataSource<Category>();
  displayedColumns: string[] = ['id', 'name', 'action'];

  constructor(
    private categoryService: CategoryService,
    public dialog: MatDialog,
  ) { }

  // lo que se carga nada mas pulsar en categorias (los datos)
  ngOnInit(): void {
    this.categoryService.getCategories().subscribe(
      categories => this.dataSource.data = categories   // categories es el dato recibido desde el backend
      // y el parametro de la funcion, en el metodo(=>) se asignan los datos a la tabla (dataSource) del front
    );
  }

  //  metodo para crear una nueva categoria -> abre componente category-edit en nueva ventana (dialog),
  // se le pasa unos datos de creacion (data:{}) y se suscribe el dialogo al evento afterClosed(),
  // para ejecutar, en este caso, una accion para volver a cargar el listado inicial
  createCategory() {    
    const dialogRef = this.dialog.open(CategoryEditComponent, {
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });    
  }  

  // es lo mismo que el create solo q pasandole una categoria desde el boton del html
  editCategory(category: Category) {
    const dialogRef = this.dialog.open(CategoryEditComponent, {
      data: { category: category }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.ngOnInit();
    });
  }

  deleteCategory(category: Category) {    
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: { title: "Eliminar categoría", description: "Atención si borra la categoría se perderán sus datos.<br> ¿Desea eliminar la categoría?" }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {   // si el dialogo de confirmacion devuelve true 
        this.categoryService.deleteCategory(category.id).subscribe(result => {  // llama al servicio de eliminar
          this.ngOnInit();
        }); 
      }
    });
  }  

}
