(ns scf.field-test
  (:require [cljs.test :refer [deftest is testing]]
            [scf.field :as subject]
            [scf.state :as state]))


(deftest label

  (testing "With a label specified"
    (is (= "Hello, World!" (subject/label {:name :goodbye
                                           :label "Hello, World!"}))))
  (testing "Without a label specified"
    (is (= "Asdf Qwerty Foo" (subject/label {:name :asdf-qwerty_foo})))))

(deftest wrap-in

  (testing "With a keyword path"
    (is (= [:div#wrapper [:a "Hello, World!"]]
           (subject/wrap-in [:a "Hello, World!"] :div#wrapper))))


  (testing "With a vector path"
    (is (= [:div#one [:p.two [:span#three [:a "Hello, World!"]]]]
           (subject/wrap-in [:a "Hello, World!"] [:div#one :p.two :span#three]))))

  (testing "With a function path"
    (is (= [:span.dynamic [:a "Hello, World!"]]
           (subject/wrap-in [:a "Hello, World!"] (fn [elem]
                                                   [:span.dynamic elem]))))))


(deftest layout
  (let [field {:type :text, :label "My Field"}
        input [:input {:value "hopscotch"}]]

    (testing "default"
      (is (= [:div {:class "scf-field"}
              [:h2 {:class "scf-label"} "My Field"]
              input]
             (subject/layout field input))))

    (testing "label-above"
      (is (= [:div {:class "scf-field"}
              [:h2 {:class "scf-label"} "My Field"]
              input]
             (subject/layout (conj field {:layout :label-above}) input))))

    (testing "label-before"
      (is (= [:div {:class "scf-field"}
              [:span {:class "scf-label"} "My Field"]
              input]
             (subject/layout (conj field {:layout :label-before}) input))))

    (testing "label-after"
      (is (= [:div {:class "scf-field"}
              input
              [:span {:class "scf-label"} "My Field"]]
             (subject/layout (conj field {:layout :label-after}) input))))))

(deftest attrs
  (let [config {:type "whatever"
                :path [:one 2]
                :attrs {:name "my_form_field"
                        :data-custom "my data"}}
        ui-state (atom {:one [{} {} {:value 3}]})]

    (testing "standard preset"
      (let [attrs (subject/attrs (conj config {:attr-preset :standard})
                                 ui-state
                                 {:type "text"
                                  :class "fancy"})]
        (is (= {:type "text"
                :class "fancy"
                :value 3
                :name "my_form_field"
                :data-custom "my data"}
               ; don't try to compare the :on-change handlers
               (dissoc attrs :on-change)))
        (is (fn? (:on-change attrs)))))))
