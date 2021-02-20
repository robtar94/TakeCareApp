library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

# Define UI for application that draws a histogram
klasyui <- function(id){
  ns <- NS(id)
  fluidPage(
    
    
    
    fluidRow(
      column(3,
             tags$div("Panel sterowania") %>% tagAppendAttributes(class="panel-title"),
             wellPanel(
               sliderInput("sliderKlas1",
                           "Wiek pacjenta",
                           min = 1,
                           max = 100,
                           value = 1),
               selectInput("selectKlas1",strong("Zaburzenia połykania"),choices = list("Nie"=0,"Tak"=1),selected=0),
               selectInput("selectKlas2",strong("Ból przy połykaniu"),choices = list("Nie"=0,"Tak"=1),selected=0),
               selectInput("selectKlas3",strong("Kaszel"),choices = list("Nie"=0,"Tak"=1),selected=0),
               selectInput("selectKlas4",strong("Duszności i świszczący oddech"),choices = list("Nie"=0,"Tak"=1),selected=0),
               selectInput("selectKlas5",strong("Odkrztuszanie wydzieliny z krwią i chrypka"),choices = list("Nie"=0,"Tak"=1),selected=0),
               selectInput("selectKlas6",strong("Guz w obrębie gruczołu piersiowego"),choices = list("Nie"=0,"Tak"=1),selected=0),
               selectInput("selectKlas7",strong("Zmiany skórne wokół brodawki"),choices = list("Nie"=0,"Tak"=1),selected=0),
               selectInput("selectKlas8",strong("Wyciek z brodawki (zwłaszcza krwisty)"),choices = list("Nie"=0,"Tak"=1),selected=0),
               downloadButton("report1", "Generuj raport")
               )
             
             
      )%>% tagAppendAttributes(id = 'column-panel'),
      column(9,
             tags$div("Klasyfikator zachorowalności na nowotwory") %>% tagAppendAttributes(class="panel-title"),
             wellPanel(
               plotlyOutput("distPlot")
             )
      )%>% tagAppendAttributes(id = 'column-content')
    ) %>% tagAppendAttributes(id = 'row-content'),
    fluidRow(
      column(12,
             tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
      
    )%>% tagAppendAttributes(id = 'row-footer')
  
  # Application title
  # titlePanel("Klasyfikator"),
  # 
  # # Sidebar with a slider input for number of bins 
  # sidebarLayout(
  #   sidebarPanel(
  #     sliderInput("sliderKlas1",
  #                 "Wiek pacjenta",
  #                 min = 1,
  #                 max = 100,
  #                 value = 1),
  #     selectInput("selectKlas1",strong("Zaburzenia polykania"),choices = list("Nie"=0,"Tak"=1),selected=0),
  #     selectInput("selectKlas2",strong("Bol przy polykaniu"),choices = list("Nie"=0,"Tak"=1),selected=0),
  #     selectInput("selectKlas3",strong("Kaszel"),choices = list("Nie"=0,"Tak"=1),selected=0),
  #     selectInput("selectKlas4",strong("Dusznosci i swiszczacy oddech"),choices = list("Nie"=0,"Tak"=1),selected=0),
  #     selectInput("selectKlas5",strong("Odkrztuszanie wydzieliny z krwia i chrypka"),choices = list("Nie"=0,"Tak"=1),selected=0),
  #     selectInput("selectKlas6",strong("Guz w obrebie gruczolu piersiowego"),choices = list("Nie"=0,"Tak"=1),selected=0),
  #     selectInput("selectKlas7",strong("Zmiany skorne wokol brodawki."),choices = list("Nie"=0,"Tak"=1),selected=0),
  #     selectInput("selectKlas8",strong("Wyciek z brodawki (zwlaszcza krwisty)"),choices = list("Nie"=0,"Tak"=1),selected=0)
  #     
  #   ),
  #   
  #   # Show a plot of the generated distribution
  #   mainPanel(
  #     plotlyOutput("distPlot")
  #   )
  # )
)

}

#ploc krtani piersi,zdrowy
# Define server logic required to draw a histogram
klasyserver <- function(input, output,session) {
  
  output$distPlot <- renderPlotly({
    k=(0.01*as.numeric(input$sliderKlas1)+0.1*as.numeric(input$selectKlas1)+0.1*as.numeric(input$selectKlas2))*100
    if(k>100){
      k=100
    }
    p=(0.01*as.numeric(input$sliderKlas1)+0.1*as.numeric(input$selectKlas3)+0.1*as.numeric(input$selectKlas4)+0.1*as.numeric(input$selectKlas5))*100
    if(p>100){
      p=100
    }
    #print(p*100)
    pi=(0.01*as.numeric(input$sliderKlas1)+0.1*as.numeric(input$selectKlas6)+0.1*as.numeric(input$selectKlas7)+0.1*as.numeric(input$selectKlas8))*100
    if(pi>100){
      pi=100
    }
    #print(pi*100)
    z=100-(k+p+pi)/3
    x=c("Rak krtani","Rak piersi","Rak płuc","Zdrowy")
    y=c(k,pi,p,z)
    d=data.frame(x,y)
    #print(d)
    #z=0.0029*as.numeric(input$slider1)
    
    g=ggplot(d, aes(x,y,fill=x))+ 
      geom_col()+
      labs(x="",y="Prawdopodobieństwo [%]")
    ggplotly(g)
    
    
  })
  
  output$report1 <- downloadHandler(
    
    filename = "raport.pdf",
    content = function(file) {
      
      tempReport <- file.path(tempdir(), "report1.Rmd")
      file.copy("report1.Rmd", tempReport, overwrite = TRUE)
      
      k=(0.01*as.numeric(input$sliderKlas1)+0.1*as.numeric(input$selectKlas1)+0.1*as.numeric(input$selectKlas2))*100
      if(k>100){
        k=100
      }
      p=(0.01*as.numeric(input$sliderKlas1)+0.1*as.numeric(input$selectKlas3)+0.1*as.numeric(input$selectKlas4)+0.1*as.numeric(input$selectKlas5))*100
      if(p>100){
        p=100
      }
      #print(p*100)
      pi=(0.01*as.numeric(input$sliderKlas1)+0.1*as.numeric(input$selectKlas6)+0.1*as.numeric(input$selectKlas7)+0.1*as.numeric(input$selectKlas8))*100
      if(pi>100){
        pi=100
      }
      #print(pi*100)
      z=100-(k+p+pi)/3
      
      params <- list(n = k,k=p,l=pi,m=z)
      
      
      rmarkdown::render(tempReport, output_file = file,
                        params = params,
                        envir = new.env(parent = globalenv())
      )
    }
  )
}

