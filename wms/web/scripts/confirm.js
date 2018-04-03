/*
 * Common dialogue() function that creates our dialogue qTip.
 * We'll use this method to create both our prompt and confirm dialogues
 * as they share very similar styles, but with varying content and titles.
 */
function dialogue(content, title) {
   /* 
    * Since the dialogue isn't really a tooltip as such, we'll use a dummy
    * out-of-DOM element as our target instead of an actual element like document.body
    */
   $('<div />').qtip(
   {
      content: {
         text: content,
         title: title
      },
      position: {
         my: 'center', at: 'center', // Center it...
         target: $(window) // ... in the window
      },
      show: {
         ready: true, // Show it straight away
         modal: {
            on: true, // Make it modal (darken the rest of the page)...
            blur: false // ... but don't close the tooltip when clicked
         }
      },
      hide: false, // We'll hide it maunally so disable hide events
      style: 'ui-tooltip-light ui-tooltip-rounded ui-tooltip-dialogue', // Add a few styles
      events: {
         // Hide the tooltip when any buttons in the dialogue are clicked
         render: function(event, api) {
            $('button', api.elements.content).click(api.hide);
         },
         // Destroy the tooltip once it's hidden as we no longer need it!
         hide: function(event, api) { api.destroy(); }
      }
   });
}

function confirmQtip(question, title, btnOkTxt, btnCancelTxt, callback){
	// Content will consist of the question and ok/cancel buttons
    var message = $('<p />', { text: question }),
       ok = $('<button />', { 
          text: btnOkTxt,
          css :{"margin-top":"10px"},
          click: function() { callback(true); }
       }),
       cancel = $('<button />', { 
          text: btnCancelTxt,
          css :{"margin-top":"10px"},
          click: function() { callback(false); }
       });

    dialogue( message.add(ok).add(cancel), title );
}

