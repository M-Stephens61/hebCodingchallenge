--
-- PostgreSQL database dump
--

-- Dumped from database version 12.7
-- Dumped by pg_dump version 12.7

-- Started on 2022-01-18 11:11:18

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 84345)
-- Name: imageSearch; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "imageSearch";


ALTER SCHEMA "imageSearch" OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 204 (class 1259 OID 84354)
-- Name: detectedObjects; Type: TABLE; Schema: imageSearch; Owner: postgres
--

CREATE TABLE "imageSearch"."detectedObjects" (
    id character varying NOT NULL,
    "objectName" character varying NOT NULL,
    confidence double precision,
    "imageUploadId" character varying(32)
);


ALTER TABLE "imageSearch"."detectedObjects" OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 84346)
-- Name: imageMetaData; Type: TABLE; Schema: imageSearch; Owner: postgres
--

CREATE TABLE "imageSearch"."imageMetaData" (
    "uploadId" character varying(32) NOT NULL,
    "qualifiedPath" character varying NOT NULL
);


ALTER TABLE "imageSearch"."imageMetaData" OWNER TO postgres;

--
-- TOC entry 2695 (class 2606 OID 92536)
-- Name: detectedObjects detectedObjects_pkey; Type: CONSTRAINT; Schema: imageSearch; Owner: postgres
--

ALTER TABLE ONLY "imageSearch"."detectedObjects"
    ADD CONSTRAINT "detectedObjects_pkey" PRIMARY KEY (id);


--
-- TOC entry 2693 (class 2606 OID 84353)
-- Name: imageMetaData imageMetaData_pkey; Type: CONSTRAINT; Schema: imageSearch; Owner: postgres
--

ALTER TABLE ONLY "imageSearch"."imageMetaData"
    ADD CONSTRAINT "imageMetaData_pkey" PRIMARY KEY ("uploadId");


--
-- TOC entry 2696 (class 2606 OID 84362)
-- Name: detectedObjects detectedObjects_imageUploadId_fkey; Type: FK CONSTRAINT; Schema: imageSearch; Owner: postgres
--

ALTER TABLE ONLY "imageSearch"."detectedObjects"
    ADD CONSTRAINT "detectedObjects_imageUploadId_fkey" FOREIGN KEY ("imageUploadId") REFERENCES "imageSearch"."imageMetaData"("uploadId") NOT VALID;


-- Completed on 2022-01-18 11:11:18

--
-- PostgreSQL database dump complete
--

