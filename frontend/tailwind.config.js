/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'primary': '#dd2509',
        'secondary': '#e68a1d',
        'bg': '#d8dbe2',
        'dark': '#0d1321',
      },
      fontFamily: {
        'opensans': ['Open Sans', 'sans-serif'],
        'mulish': ['Mulish', 'sans-serif'],
      },
    },
  },
  plugins: [],
}