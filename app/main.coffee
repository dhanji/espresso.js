require "espresso/web"
require "espresso/template"

match '/'
  get: ->
    markdown 'index'

  post: ->
    "bye"
