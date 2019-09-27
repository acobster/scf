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
