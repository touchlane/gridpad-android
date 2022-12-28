Change Log
==========

## Version 1.0.0

* **API Change**: to `GridPad` added `placementPolicy` property
* **API Change**: `GridPadScope.item()` split to two methods - explicit and implicit
* **Behavior Change**: implicit placement of elements depends on `placementPolicy` property
* **Behavior Change**: span expands depends on `placementPolicy` property

The new API shouldn't affect your code by default. Only one case can be affected by library 
update - cases when only one of the `row` or `column` parameters was defined in GridPad.item. 
This way of using is no more acceptable for not enough clear behavior reasons.

## Version 0.0.4

_2022-12-20_

* **API Change**: GridPad DSL is limited to a Kotlin context receiver
* **Fix**: size distribution algorithm
* test coverage improvement

## Version 0.0.3

_2022-12-09_

* **Fix**: added missing source code

## Version 0.0.2

_2022-12-07_

* **Remove**: unused dependencies

## Version 0.0.1

_2022-12-07_

* First alpha release
