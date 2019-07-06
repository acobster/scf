# Simple Custom Fields

> A tip o' the hat to the excellent [Advanced Custom Fields](https://www.advancedcustomfields.com/) WordPress plugin by Elliot Condon, for Clojure/Script

## TODO

### Basic field types

* Text input
* Textarea
* Range
* Checkbox (with stylized ACF-like True/False option)
* Radio (group)
* Select
* File
* Link

### Organizational UI Components (i.e. higher-order fields)

* Tab group
* Accordion
* Repeater
* Arbitrary container HTML? (to effectively achieve what [Groups](https://www.advancedcustomfields.com/resources/group/) achieve, but more flexibly?)

For reference: https://www.advancedcustomfields.com/resources/

### Conditional Fields

As in ACF, you can define when certain fields are displayed based on the state of other fields. Just define a function at the `:display?` key in your field config:

```clojure
{:type :button
 :display? (fn [ui-state]
 						 (= "some-value"
                (get-in ui-state [:some :other :field :path]))}
```

The function can actually be any arbitrary function, and doesn't necessarily have to examine the `ui-state` at all, although that's probably the most typical use-case.

### Validations

Built-in validators:

* `:required`
* ???

To attach custom validators to a given field, include a `:valid?` key:

```clojure
{:name :your-name
 :type :text
 :valid? (fn [value ui-state]
           (if (= "Bob" value)
             true
             {:error "Your name must be Bob"}))}
```

If the function at `:valid?` returns `true`, the field is considered valid. If a map is returned, the value of the map's `:error` key is displayed as an error message. Currently, `:error` is the only meaningful key that can be returned inside a map, but other keys may be supported in the future.

## Development and Contribution

Don't be a jerk.

### Development mode

To start the compiler, navigate to the project folder and run the following command in the terminal:

```
shadow-cljs watch dev
```

### Building for production

```
shadow-cljs release prod
```
