library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

homeUI <- function(id) {
  ns <- NS(id)
  fluidPage(
  tags$head(
    tags$style(HTML("
      @import url('//fonts.googleapis.com/css?family=Lobster|Cabin:400,700');
      @import url('//fonts.googleapis.com/css2?family=Fjalla+One');

                    "))),
  
  theme = "style.css",
  

  
  fluidRow(column(12,
                  h1("TakeCareApp"))%>% tagAppendAttributes(id = 'column-title')
  ) %>% tagAppendAttributes(id = 'row-title'),

  fluidRow(column(4,
                  div(div("Monitoruj swój stan zdrowia"),img(src="observ.png",height = 150, width = 150)))%>% tagAppendAttributes(class = 'column-tile'),
           column(4,
                  div(div("Generuj raporty"),img(src="raport.png",height = 150, width = 150)))%>% tagAppendAttributes(class = 'column-tile')
           
  ) %>% tagAppendAttributes(id = 'row-tiles'),
  
  
  
  fluidRow(
    column(12,
           tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
    
  )%>% tagAppendAttributes(id = 'row-footer')
  
  
)
}
homeServer <- function(input, output) {
  


}

