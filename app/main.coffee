require "espresso/web"
require "espresso/template"

match '/'
  get: ->
    body: markdown 'index'

  post: ->
    "bye"
