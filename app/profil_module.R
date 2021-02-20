library(shiny)
library(magrittr)
library(ggplot2)
library(plotly)
library(DT)

profilUI <- function(id) {
  ns <- NS(id)
  fluidPage(
    useShinyjs(),
    tags$head(
      tags$script(src="js.cookie.js"),
      tags$script('Shiny.addCustomMessageHandler("tokenHandlerUpdate",     
    function(token) {
     sessionStorage.setItem(\'token\', token);
     Shiny.onInputChange("token", token);
    }
);'),
      tags$script('Shiny.addCustomMessageHandler("profileActiveTabHandler",     
    function(arg) {
     Shiny.onInputChange("profileActiveTab", 1);
    }
);'),
      tags$script('Shiny.addCustomMessageHandler("viewPage",     
    function(page,token,auth) {
    Shiny.onInputChange("token", token);
    Shiny.onInputChange("auth", auth);

    window.location.href=page;
    }
);'),
      
      
      tags$style(HTML("
      @import url('//fonts.googleapis.com/css?family=Lobster|Cabin:400,700');
      @import url('//fonts.googleapis.com/css2?family=Fjalla+One');
      
                    ")),
      tags$link(rel = "stylesheet", type = "text/css", href = "profile.css")
    ),
    
    # theme = "style.css",
    
    
    # App title ----
    
    uiOutput("afterLogin"),
    
    
    
    
    fluidRow(
      column(12,
             tags$span("Copyright Wszystkie prawa zastrzeżone."))%>% tagAppendAttributes(id = 'column-copyright'),
      
    )%>% tagAppendAttributes(id = 'row-footer')
    
    
  )}

profilServer <- function(input, output,session) {
  
  shinyInput <- function(FUN, len, id, ...) {
    inputs <- character(len)
    for (i in seq_len(len)) {
      inputs[i] <- as.character(FUN(id, ...))
    }
    inputs
  }
  
  
  
  getEditStatus <- eventReactive(input$editSubmit, {
    editedPersonalData<-data.frame(name<-input$editName,
                                   surname<-input$editSurname,
                                   mail<-input$editMail,
                                   datebirth<-input$editAge,
                                   gender<-input$editGender)
    
    # reg<-c(grepl("^[A-Z][a-zA-ZĂ„â€žĂ„â€¦Ă„â€ Ă„â€ˇĂ„ÂĂ„â„˘ÄąÂÄąâ€šÄąÂÄąâ€žÄ‚â€śÄ‚Ĺ‚ÄąĹˇÄąâ€şÄąÄ…ÄąĹźÄąÂ»ÄąÄ˝]{2,15}$",editedPersonalData$name),
    #        grepl("^[A-Z][a-zA-ZĂ„â€žĂ„â€¦Ă„â€ Ă„â€ˇĂ„ÂĂ„â„˘ÄąÂÄąâ€šÄąÂÄąâ€žÄ‚â€śÄ‚Ĺ‚ÄąĹˇÄąâ€şÄąÄ…ÄąĹźÄąÂ»ÄąÄ˝]{2,20}$",editedPersonalData$surname),
    #        grepl("^[a-z]+[0-9]*@([a-z]{2,10}\\.)+[a-z]{2,5}$",editedPersonalData$mail))
    
    reg<-c(grepl("^[A-Z][a-zA-ZÄ„Ä…Ä†Ä‡ÄÄ™ĹĹ‚ĹĹ„Ă“ĂłĹšĹ›ĹąĹşĹ»ĹĽ]{2,15}$",editedPersonalData$name),
           grepl("^[A-Z][a-zA-ZÄ„Ä…Ä†Ä‡ÄÄ™ĹĹ‚ĹĹ„Ă“ĂłĹšĹ›ĹąĹşĹ»ĹĽ]{2,20}$",editedPersonalData$surname),
           grepl("^[a-z]+[0-9]*@([a-z]{2,10}\\.)+[a-z]{2,5}$",editedPersonalData$mail))
    if(all(reg)){
      
      personalData = list(
        name = editedPersonalData$name,
        surname = editedPersonalData$surname,
        email = editedPersonalData$mail,
        datebirth = editedPersonalData$datebirth,
        gender =editedPersonalData$gender)
      
      
      to_send = list(
        personalDataDTO = personalData)
      
      
      
      
      r<-httr::PUT("http://localhost:8080/api/profile",add_headers(Authorization=paste("Bearer",input$token,sep=" ")),body=to_send,encode = 'json')
      
      if (r$status_code==200){
        TRUE
      }else{
        FALSE
      }
    }else{
      
      FALSE
    }
    
    
    
  })
  
  
  
  
  personalDataVector <- reactiveVal()
  historyDataVector <- reactiveVal()
  activeTab<-reactiveVal()
  
  downloadPersonalData<-reactive({
    
    r<-httr::GET("http://localhost:8080/api/profile",add_headers(Authorization=paste("Bearer",input$token,sep=" ")))
    r
    
    
  })
  
  downloadHistoryData<-reactive({

    r<-httr::GET("http://localhost:8080/api/prediction/usersPredictions/ind",add_headers(Authorization=paste("Bearer",input$token,sep=" ")),encode = 'json')
    print("Reactive hist")
    r
    
    
  })
  
  observe({
    currPage = get_page()
    if(currPage=="profil" | currPage=="calculator" | currPage=="iota"){

      session$sendCustomMessage(type='tokenHandlerAccess',(session$clientData)$url_hash)
    }
    
  })
  
  
  observeEvent(input$profileTabs,{


    
    if(input$profileTabs=="data"){

      personalDataVector(downloadPersonalData())
    }else if(input$profileTabs=="history"){

      historyDataVector(downloadHistoryData())
      
    }
    
  })
  
  personalDataReturn <-reactive({
    personalDataVector$values
  })
  
  historyDataReturn <-reactive({
    historyDataVector()
  })
  
  observeEvent(input$del_button,{
    r<-httr::DELETE(paste("http://localhost:8080/api/prediction/delete/",input$del_button,sep = ""),add_headers(Authorization=paste("Bearer",input$token,sep=" ")),encode = 'json')
    
    if(r$status_code==200){
      rr<-httr::GET("http://localhost:8080/api/prediction/usersPredictions/ind",add_headers(Authorization=paste("Bearer",input$token,sep=" ")),encode = 'json')
      
      historyDataVector(rr)
      
    }
    
  })
  
  output$profileData<-renderUI({
    r <- personalDataVector()
    
    if(length(r)!=0 & !is.null(r)){
      
      if(r$status_code==200){
        
        response<-(content(r))
        
        
        session$sendCustomMessage(type='tokenHandlerUpdate', response$token)
        
        
        fluidRow(column(12,
                        wellPanel(
                          textInput("editName", label = strong("Imię"),value=response$profil$personalDataDTO$name),
                          uiOutput("editName"),
                          textInput("editSurname", label = strong("Nazwisko"),value=response$profil$personalDataDTO$surname),
                          uiOutput("editSurname"),
                          textInput("editMail", label = strong("Adres email"),value=response$profil$personalDataDTO$email),
                          uiOutput("editMail"),
                          dateInput("editAge", label = strong("Data urodzenia") ,value=response$profil$personalDataDTO$datebirth),
                          
                          selectInput("editGender", label = strong("Płeć"),
                                      choices = list("Żeńska" = 0, "męska" = 1), 
                                      selected = as.numeric(response$profil$personalDataDTO$gender)),
                          
                          
                          actionButton("editSubmit","Zapisz"),
                          uiOutput("btnEditProfile",style="color:red;")
                          
                        ),
                        
                        
        ))
        
        
      }
      
      
    }
    
    
  })
  output$btnEditProfile<-renderUI({
    
    if (getEditStatus()==TRUE){
      p("OK",style="color:green;text-align:center;")
    }else{
      p("Użytkownik istnieje lub wprowadzono błędne dane",style="color:red;text-align:center;")
    }
    
    
  })
  
  
  output$afterLogin<-renderUI({
    if(get_page()=="profil"){
    if(!is.null(input$auth) & length(input$auth)>0 ){
      fluidRow(
        
        column(12,
               tabsetPanel(id="profileTabs",type = "tabs",
                           tabPanel("Dane profilowe",value="data", tags$div(uiOutput("profileData")
                           ) %>% tagAppendAttributes(id = 'content-personal')),
                           
                           tabPanel("Historia pomiarów",value='history', tags$div(DT::dataTableOutput("historyTable",height = "auto"))%>% tagAppendAttributes(id="profileTabs",class = 'content-wrapper'))
                           
               ))%>% tagAppendAttributes(id = 'column-profile')
        
      ) %>% tagAppendAttributes(id = 'row-content')
    }else{
      
    }}
    
    
  })
  
  
  output$plot1 <- renderPlotly({
    
    
    g<-ggplot(mpg) + 
      geom_point(mapping = aes(x = displ, y = hwy))
    
    gg<-ggplotly(g)
    
    
    
    gg
    
  })
  
  observeEvent(input$view_button,{
    
    change_page(paste("?id=",input$view_button,"#!/iota",sep=""), session = session, mode = "push")
    
  })
  
  output$historyTable <-  DT::renderDataTable({
    r <- historyDataVector()
    
    if(is.null(content(r)$predictions)){
      DT::datatable(data.frame(Nazwa=character(),Wynik=numeric(),Data=character()),options = list(scrollX = TRUE,language=pl))
    }else{
      df_historyTable<-as.data.frame(do.call(rbind, (content(r)$predictions)))
      
      historyTableButtons = list()
      
      for(rowNumber in 1:nrow(df_historyTable)){
      historyTableButtons[[rowNumber]] <-list(shinyInput(actionButton, 1, as.character(df_historyTable[rowNumber,]$id[1]), label = "Pokaż", onclick = 'Shiny.onInputChange(\"view_button\",  this.id)' ),
           shinyInput(actionButton, 1, as.character(df_historyTable[rowNumber,]$id[1]), label = "Usuń", onclick = 'Shiny.onInputChange(\"del_button\",  this.id)'))
      }
    }

    df_historyTable<-df_historyTable %>%
      mutate(Akcja=historyTableButtons)%>%
      select(name,resultValue,localDateTime,Akcja) %>%
      mutate(localDateTime = (str_replace(strptime(str_replace(localDateTime,"T"," "),"%Y-%m-%d %H:%M:%S")," CET","")))%>%
      rename(
        Nazwa = name,
        Wynik = resultValue,
        Data = localDateTime
      )
    

    df_historyTable$Nazwa<-do.call(c, df_historyTable$Nazwa)
    df_historyTable$Wynik<-do.call(c, df_historyTable$Wynik)

    DT::datatable(df_historyTable,selection="none",options = list(scrollX = TRUE,language=pl))
    # }
    
  })
  
  output$plot3 <- renderPlotly({
    
    g<-ggplot(data = mpg, mapping = aes(x = displ, y = hwy, color = drv)) + 
      geom_point() + 
      geom_smooth(se = FALSE)
    
    gg<-ggplotly(g)
    
    
    
    gg
    
  })
  
  output$plot4 <- renderPlotly({
    
    g<-ggplot(data = mpg, mapping = aes(x = displ, y = hwy)) + 
      geom_point(mapping = aes(color=drv)) + 
      geom_smooth(se = FALSE)
    
    gg<-ggplotly(g)
    
    gg
    
  })
  
  pl <- list(
    emptyTable="Tabela jest pusta",
    sSearch = "Szukaj",
    sInfo="Wyniki od _START_ do _END_ z _TOTAL_ rekordów",
    sZeroRecords="Brak rekordów",
    sEmptyTable="Pusta tabela",
    oPaginate= list(
      sFirst="Pierwsza", sPrevious="Poprzednia",sLast="Ostatnia", sNext="Następna"
    ),
    sLengthMenu = "Pokaż _MENU_ rekordów na stronie"
  )
  
  output$table1 <- DT::renderDataTable(iris,options = list(scrollX = TRUE,language=pl))
  
  
  output$btnResponse<-renderUI({
    
    if (getStatus()==TRUE){
      p("OK",style="color:white;text-align:center;")
    }else{
      p("Użytkownik istnieje lub wprowadzono błędne dane",style="color:yellow;text-align:center;")
    }
    
    
  })
  
  output$editName<-renderUI({
    s<-toString(input$editName)
    
    if (s=="" | grepl("^[A-Z][a-zA-ZĂ„â€žĂ„â€¦Ă„â€ Ă„â€ˇĂ„ÂĂ„â„˘ÄąÂÄąâ€šÄąÂÄąâ€žÄ‚â€śÄ‚Ĺ‚ÄąĹˇÄąâ€şÄąÄ…ÄąĹźÄąÂ»ÄąÄ˝]{2,15}$",s)==TRUE){
      return()
    }else{
      p("Bład: Imię powinno zaczynać się od wielkiej litery, zawierać jedynie litery i mieć długość od 3 do 15 znaków",style="color:yellow")
    }
    
  })
  
  output$editSurname<-renderUI({
    s<-toString(input$editSurname)
    
    if (s=="" | grepl("^[A-Z][a-zA-ZĂ„â€žĂ„â€¦Ă„â€ Ă„â€ˇĂ„ÂĂ„â„˘ÄąÂÄąâ€šÄąÂÄąâ€žÄ‚â€śÄ‚Ĺ‚ÄąĹˇÄąâ€şÄąÄ…ÄąĹźÄąÂ»ÄąÄ˝]{2,20}$",s)==TRUE){
      return()
    }else{
      p("Bład: Nazwisko powinno zaczynać sie od wielkiej litery, zawierać jedynie litery i mieć długość od 3 do 15 znakĂłw",style="color:yellow")
      
      
    }
    
  })
  
  output$editMail<-renderUI({
    s<-toString(input$editMail)
    
    if (s=="" | grepl("^[a-z]+[0-9]*@([a-z]{2,10}\\.)+[a-z]{2,5}$",s)==TRUE){
      return()
    }else{
      p("Bład: Mail powinien mieć budowę adres@nazwa.domena",style="color:yellow")
      
      
    }
    
  })
  
  
  
  
}
