/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        darkBg: '#0b0e11',        
        darkCard: '#181a20',     
        darkHover: '#2b313a',    
        darkBorder: '#2b313a',   
        cryptoYellow: '#fcd535',  
        cryptoGreen: '#0ecb81',  
        cryptoRed: '#f6465d',   
        textMuted: '#848e9c',     
      }
    },
  },
  plugins: [],
}
