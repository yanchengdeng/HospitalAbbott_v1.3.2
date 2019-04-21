package com.comvee.hospitalabbott.bean;

public  class PagerBean {
        /**
         * currentPage : 1
         * endRow : 2
         * firstPage : false
         * getCount : true
         * lastPage : true
         * pageSize : 2
         * startRow : 0
         * totalPages : 10
         * totalRows : 20
         */

        private int currentPage;
        private int endRow;
        private boolean firstPage;
        private boolean getCount;
        private boolean lastPage;
        private int pageSize;
        private int startRow;
        private int totalPages;
        private int totalRows;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isGetCount() {
            return getCount;
        }

        public void setGetCount(boolean getCount) {
            this.getCount = getCount;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }
    }