<h1 align="center">GridPad Jetpack Compose layout</h1>

:date: GridPad is a Jetpack Compose library that allows you to place UI elements in a predefined
grid, manage spans in two dimensions, have flexible controls to manage row and column sizes.

# Usage

GridPad combines LazyRow/LazyColumn and LazyVerticalGrid/LazyHorizontalGrid APIs but has some
differences and additional functionality to control content on the layout.

Key features and limitations:

* Follows [slot API](https://developer.android.com/jetpack/compose/layouts/basics#slot-based-layouts) concept.
* Not lazy. All content will be measured and placed instantly.
* GridPad must have limited bounds.
* It's possible to specify the exact place on the grid for each element.
* One cell can be placed in more than one element. The draw order will be the same as the place
  order.
* Each item in a cell can have different spans.
* Each item can have horizontal and vertical spans simultaneously.
* Each row and column can have a specific size: fixed or weight-based.

## Define the grid

Specifying exact grid size is required but specifying each row and column size is optional.
The following code initializes 3x4 grid with rows and columns weights equal to 1:

```kotlin
GridPad(cells = GridPadCells(rowCount = 3, columnCount = 4)) {
    // content        
}
```

By default rows and columns have **weight** size equal to 1, but it's possible specify different
size to specific row or column.
The library support 2 types of sizes:

* **GridPadCellSize.Fixed** - fixed size in Dp, not change when the bounds of GridPad change.
* **GridPadCellSize.Weight** - relative, depends on other weights, remaining space after placing
  fixed sizes and the GridPad bounds.

To define specific for row or column you need to use `GridPadCells.Builder` API:

```kotlin
GridPad(
    cells = GridPadCells.Builder(rowCount = 3, columnCount = 4)
        .rowSize(index = 0, size = GridPadCellSize.Weight(2f))
        .columnSize(index = 3, size = GridPadCellSize.Fixed(32.dp))
        .build()
) {
    // content
}
```

The algorithm for allocating available space between cells:

1. All fixed (**GridPadCellSize.Fixed**) values are subtracted from the available space.
2. The remaining space is allocated between the remaining cells according to their weight value.

## Place the items

# License

```
MIT License

Copyright (c) 2022 Touchlane LLC tech@touchlane.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```