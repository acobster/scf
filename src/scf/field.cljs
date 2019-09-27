(ns scf.field
  (:require [clojure.string :as string]))


(defn label [{field-label :label field-name :name}]
  (or
    field-label
    (as-> field-name v            ; :field-name
        (name v)                  ; "field-name"
        (string/split v #"[-_]")  ; ["field" "name"]
        (map string/capitalize v) ; ["Field" "Name"]
        (string/join " " v))))    ; "Field Name"

(defn wrap-in [html path]
  "Takes an element and a path, and returns the element wrapped in nested
  elements, each of which is one \"step\" in path"
  (cond
    (keyword? path) [path html]
    (fn? path) (path html)
    :else (reduce (fn [wrapped wrapper] [wrapper wrapped]) html (reverse path))))
