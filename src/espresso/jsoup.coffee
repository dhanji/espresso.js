# Expose Jsoup to espresso apps

global.jsoup = (html) ->
  new Packages.org.jsoup.Jsoup.parse(html)