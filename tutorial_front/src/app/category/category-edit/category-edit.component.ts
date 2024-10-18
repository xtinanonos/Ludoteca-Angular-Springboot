import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from '../category.service';
import { Category } from '../model/Category';

@Component({
  selector: 'app-category-edit',
  templateUrl: './category-edit.component.html',
  styleUrls: ['./category-edit.component.scss']
})
export class CategoryEditComponent implements OnInit {

  category : Category;

  constructor(
    public dialogRef: MatDialogRef<CategoryEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {
    if (this.data.category != null) {
      // this.category = this.data.category;  // si queremos q se modifique el objeto en el listado
      this.category = Object.assign({}, this.data.category);  // se hace una copia del objeto para q no se modifique el objeto del listado
    }
    else {
      this.category = new Category();
    }
  }

  onSave() {
    this.categoryService.saveCategory(this.category).subscribe(result => {
      this.dialogRef.close();
    });    
  }  

  onClose() {
    this.dialogRef.close();
  }

}
