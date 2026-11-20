import type { Config } from 'tailwindcss'
export default {
  content: ['./index.html','./src/**/*.{js,ts,jsx,tsx}'],
  theme: { extend: {
    colors: { ink:'#050A14', panel:'#0A0F1E', blue:'#00AFFF', cyan:'#00E5FF', text:'#E6EDF3', muted:'#8B98AD' },
    fontFamily: { sans:['Inter','sans-serif'], display:['Space Grotesk','sans-serif'] }
  }},
  plugins: []
} satisfies Config
