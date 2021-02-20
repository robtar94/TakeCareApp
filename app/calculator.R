library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

calculatorUI <- function(id){
  ns <- NS(id)
  uiOutput("calculatorPage")

}

calculatorServer <- function(input, output, session) {
  calculatorRV <-reactiveValues(value=NULL)
  calculatorTV <-reactiveValues(value=NULL)
  
  
  output$calculatorPage<-renderUI({
    if(get_page()=="calculator"){
    
    fluidPage(
      
      
      fluidRow(
        column(3,
               tags$div("Panel sterowania") %>% tagAppendAttributes(class="panel-title"),
               wellPanel(
                 sliderInput("slider1", strong("Wiek pacjenta:"),min = 14, max = 100, value = 40),
                 selectInput("select1",strong("Obecność wodobrzusza:"),choices = list("Nie"=0,"Tak"=1),selected=0),
                 selectInput("select2",strong("Obecność przepływu krwi w projekcji brodawkowatej:"),choices = list("Nie"=0,"Tak"=1),selected=0),
                 sliderInput("slider2", strong("Największa średnica elementu stałego (w mm):"),min = 0, max = 200, value = 0),
                 selectInput("select3",strong("Nieregularna wewnętrzna ściana torbieli:"),choices = list("Nie"=0,"Tak"=1),selected=0),
                 selectInput("select4",strong("Obecność cieni akustycznych:"),choices = list("Nie"=0,"Tak"=1),selected=0),
                 actionButton("update" ,"Oblicz"),
                 downloadButton("report", "Generuj raport"))
               
               
        )%>% tagAppendAttributes(id = 'column-panel'),
        column(9,
               tags$div("Kalkulator wskaźnika ryzyka nowotworu jajnika (IOTA LR2)") %>% tagAppendAttributes(class="panel-title"),
               wellPanel(
                 p("Aplikacja przeznaczona jest dla lekarzy ginekologów i wdraża wskaźnik złośliwości nowotworu jajnika w oparciu o algorytm IOTA LR2. Wizualizuje również wynik regresji logistycznej."),
                 p("Szczegółowy opis algorytmu znajduje się w artykule: Timmerman D, Testa AC, Bourne T, [i in.]. Model regresji logistycznej do rozróżniania łagodnych i złośliwych guzów przydatków przed operacją: wieloośrodkowe badanie przeprowadzone przez International Ovarian Tumor Analysis Group. J Clin Oncol. 2005, 23, 8794-8801."),
                 p("Ogólnie algorytm LR2 przewiduje, że nowotwór jest łagodny, gdy pacjent jest młody, lity składnik zmiany jest mały i występują cienie akustyczne. Możesz to sprawdzić empirycznie za pomocą różnych kombinacji wartości wejściowych."),
                 p("Wypełnij formularz i kliknij",strong("Oblicz")," "),
                 
                 htmlOutput("selected_var"),
                 htmlOutput("var"),
                 br(),
                 plotlyOutput("wykres"),
                 uiOutput("calculatorSave")
               )
        )%>% tagAppendAttributes(id = 'column-content')
      ) %>% tagAppendAttributes(id = 'row-content'),
      fluidRow(
        column(12,
               tags$span("© Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
        
      )%>% tagAppendAttributes(id = 'row-footer')
      
      
      
    )
    }
    
    
  })
  
  output$report <- downloadHandler(

    filename = "raport.pdf",
    content = function(file) {

      tempReport <- file.path(tempdir(), "report.Rmd")
      file.copy("report.Rmd", tempReport, overwrite = TRUE)

      p=0
      if(as.numeric(input$slider2)>=50){
        p=50
      }
      z=-5.3718+0.0354*as.numeric(input$slider1)+1.6159*as.numeric(input$select1)+1.1768*as.numeric(input$select2)+0.0697*p+0.9586*as.numeric(input$select3)-2.9486*as.numeric(input$select4)
      x=round(1/(1+exp(-z)),3)
      params <- list(n = input$slider1,k=input$slider2,l=input$select1,m=input$select2,p=input$select3,r=input$select4,z=x)
      

      rmarkdown::render(tempReport, output_file = file,
                        params = params,
                        envir = new.env(parent = globalenv())
      )
    }
  )

  output$selected_var <- renderText({
    input$update
    p=0
    if(as.numeric(isolate(input$slider2))>=50){
      p=50
    }
    z=-5.3718+0.0354*as.numeric(isolate(input$slider1))+1.6159*as.numeric(isolate(input$select1))+1.1768*as.numeric(isolate(input$select2))+0.0697*p+0.9586*as.numeric(isolate(input$select3))-2.9486*as.numeric(isolate(input$select4))
    x=round(1/(1+exp(-z)),3)
    calculatorRV$value<-x
    if(as.numeric(input$update)>0){
      paste("Surowa wartość predyktora (im niższa, tym lepiej): ", strong(x))
    }
    
  })
  
  output$var <- renderText({
    input$update
    p=0
    if(as.numeric(isolate(input$slider2))>=50){
      p=50
    }
    z=-5.3718+0.0354*as.numeric(isolate(input$slider1))+1.6159*as.numeric(isolate(input$select1))+1.1768*as.numeric(isolate(input$select2))+0.0697*p+0.9586*as.numeric(isolate(input$select3))-2.9486*as.numeric(isolate(input$select4))
    x=round(1/(1+exp(-z)),3)
    if(as.numeric(input$update)>0){
    if(x>0.1){
      paste("Klasa guza: ",strong("złośliwy"))
      calculatorTV$value<-paste("Klasa guza: ",strong("złośliwy"))
    } else {
      paste("Klasa guza: ",strong("łagodny"))
      calculatorTV$value <- paste("Klasa guza: ",strong("łagodny"))
    }
    }
  })
  
  output$wykres <- renderPlotly({
    input$update
    p=0
    if(as.numeric(isolate(input$slider2))>=50){
      p=50
    }
    z=-5.3718+0.0354*as.numeric(isolate(input$slider1))+1.6159*as.numeric(isolate(input$select1))+1.1768*as.numeric(isolate(input$select2))+0.0697*p+0.9586*as.numeric(isolate(input$select3))-2.9486*as.numeric(isolate(input$select4))
    x=seq(by=1,-8,8)
    y=round(1/(1+exp(-x)),3)
    d=data.frame(x,y)
    if(as.numeric(input$update)>0){
    g=ggplot(data=d,aes(x=x,y=y))+
      geom_line()+
      geom_point(aes(x=z,y=round(1/(1+exp(-z)),3)),color="red",size=4)+
      geom_hline(aes(yintercept=0.1),linetype = "dashed")+
      geom_text(aes(x=6,y=0.15),label="próg złośliwości: 0.1")+
      labs(x="Realność",y="Prognoza")+
      theme_light()
      ggplotly(g)
    }
    
    
  })
  
  output$calculatorSave<-renderUI({
    if(as.numeric(input$update)>0){
    
    actionButton("calculatorSubmit","Zapisz")
    }

    
  })
  
  observeEvent(input$calculatorSubmit, {


    calculatorSave<-data.frame(slider1<-input$slider1,
                                   select1<-input$select1,
                                   select2<-input$select2,
                                   slider2<-input$slider2,
                                   select3<-input$select3,
                                   select4<-input$select4)

      
    

      calculatorParameterInts = list(list(name="parameter1",value = calculatorSave$slider1),
      list(name="parameter2",value = calculatorSave$select1),
      list(name="parameter3",value = calculatorSave$select2),
      list(name="parameter4",value = calculatorSave$slider2),
      list(name="parameter5",value = calculatorSave$select3),
      list(name="parameter6",value = calculatorSave$select4))
      
      prediction = list(
        name = "IOTA",
        parameterInts = calculatorParameterInts,
        resultValue = calculatorRV$value,
        resultText = calculatorTV$value
      )


      r<-httr::POST("http://localhost:8080/api/prediction/save",add_headers(Authorization=paste("Bearer",input$token,sep=" ")),body=prediction,encode = 'json')
      
      # SPRAWDZENIE POBIERANIA JEDNEGO I WIELU POMIAROW
      # r<-httr::GET("http://localhost:8080/api/prediction/get/7",add_headers(Authorization=paste("Bearer",input$token,sep=" ")),encode = 'json')
      # r<-httr::GET("http://localhost:8080/api/prediction/usersPredictions/ind",add_headers(Authorization=paste("Bearer",input$token,sep=" ")),encode = 'json')
      
      if (r$status_code==200){
        TRUE
      }else{
        FALSE
      }
      # print(toJSON(content(r,as = "parsed")))
    



  })
  
  
}

