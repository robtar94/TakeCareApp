library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

aboutUI <- function(id) {
  ns <- NS(id)
  fluidPage(
    tags$head(
      tags$style(HTML("
      @import url('//fonts.googleapis.com/css?family=Lobster|Cabin:400,700');
      @import url('//fonts.googleapis.com/css2?family=Fjalla+One');

    .tab-link-3{      		
    background-color: #008375;
  	color: #FFFFFF !important;
  	font-family: Fjalla One;}
      
                    "))),
    
    theme = "style.css",

    # App title ----
    
    fluidRow(column(12," ABOUT")%>% tagAppendAttributes(id = 'column-content')
    ) %>% tagAppendAttributes(id = 'row-content'),
    
    
    
    fluidRow(
      column(12,
             tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
      
    )%>% tagAppendAttributes(id = 'row-footer')
    
    
  )
}
aboutServer <- function(input, output) {
  
  
  
}

